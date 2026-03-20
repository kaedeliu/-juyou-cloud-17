package com.juyou.storage.txcos;

import com.juyou.storage.config.CosConfig;
import com.juyou.storage.utils.StorageUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import com.qcloud.cos.model.StorageClass;
import com.qcloud.cos.model.UploadResult;
import com.qcloud.cos.region.Region;
import com.qcloud.cos.transfer.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class TxCosUtils {

    @Autowired
    CosConfig cosConfig;
    COSClient createCOSClient(){
        COSCredentials cred = new BasicCOSCredentials(cosConfig.getSecretId(), cosConfig.getSecretKey());
        ClientConfig clientConfig = new ClientConfig(new Region(cosConfig.getRegionName()));
        COSClient cosClient = new COSClient(cred, clientConfig);
        return cosClient;
    }

    // 创建 TransferManager 实例，这个实例用来后续调用高级接口
    TransferManager createTransferManager( COSClient cosClient) {
        // 创建一个 COSClient 实例，这是访问 COS 服务的基础实例。
        // 详细代码参见本页: 简单操作 -> 创建 COSClient


        // 自定义线程池大小，建议在客户端与 COS 网络充足（例如使用腾讯云的 CVM，同地域上传 COS）的情况下，设置成16或32即可，可较充分的利用网络资源
        // 对于使用公网传输且网络带宽质量不高的情况，建议减小该值，避免因网速过慢，造成请求超时。
        ExecutorService threadPool = Executors.newFixedThreadPool(32);

        // 传入一个 threadpool, 若不传入线程池，默认 TransferManager 中会生成一个单线程的线程池。
        TransferManager transferManager = new TransferManager(cosClient, threadPool);

        // 设置高级接口的配置项
        // 分块上传阈值和分块大小分别为 5MB 和 1MB
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(5*1024*1024);
        transferManagerConfiguration.setMinimumUploadPartSize(1*1024*1024);
        transferManager.setConfiguration(transferManagerConfiguration);

        return transferManager;
    }

    public String update(String fileName, InputStream inputStream,Long contentLength){

        COSClient cosClient = createCOSClient();

        // 使用高级接口必须先保证本进程存在一个 TransferManager 实例，如果没有则创建
        // 详细代码参见本页：高级接口 -> 创建 TransferManager
        TransferManager transferManager = createTransferManager(cosClient);

        // 存储桶的命名格式为 BucketName-APPID，此处填写的存储桶名称必须为此格式
        String bucketName = cosConfig.getBucketName();

        // 对象键(Key)是对象在存储桶中的唯一标识。
        String key = cosConfig.getRootPath()+ File.separator+ fileName;

        ObjectMetadata objectMetadata = new ObjectMetadata();
        // 上传的流如果能够获取准确的流长度，则推荐一定填写 content-length
        // 如果确实没办法获取到，则下面这行可以省略，但同时高级接口也没办法使用分块上传了
        objectMetadata.setContentLength(contentLength);

        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, key, inputStream,objectMetadata);

        // 设置存储类型（如有需要，不需要请忽略此行代码）, 默认是标准(Standard), 低频(standard_ia)
        // 更多存储类型请参见 https://cloud.tencent.com/document/product/436/33417
        putObjectRequest.setStorageClass(StorageClass.fromValue(cosConfig.getStandardClass()));

        try {
            Upload upload = transferManager.upload(putObjectRequest);
            UploadResult uploadResult = upload.waitForUploadResult();
        } catch  (Exception e) {
            e.printStackTrace();
        }
        shutdownTransferManager(transferManager);
        return returnPath(key,fileName,bucketName, cosConfig.getRootPath(), cosConfig.getRegionName());

    }

    // 可以参考下面的例子，结合实际情况做调整
    void showTransferProgress(Transfer transfer) {
        // 这里的 Transfer 是异步上传结果 Upload 的父类
        System.out.println(transfer.getDescription());

        // transfer.isDone() 查询上传是否已经完成
        while (transfer.isDone() == false) {
            try {
                // 每 2 秒获取一次进度
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return;
            }

            TransferProgress progress = transfer.getProgress();
            long sofar = progress.getBytesTransferred();
            long total = progress.getTotalBytesToTransfer();
            double pct = progress.getPercentTransferred();
            System.out.printf("upload progress: [%d / %d] = %.02f%%\n", sofar, total, pct);
        }

        // 完成了 Completed，或者失败了 Failed
        System.out.println(transfer.getState());
    }

    void shutdownTransferManager(TransferManager transferManager) {
        // 指定参数为 true, 则同时会关闭 transferManager 内部的 COSClient 实例。
        // 指定参数为 false, 则不会关闭 transferManager 内部的 COSClient 实例。
        transferManager.shutdownNow(true);
    }

    /**
     * 获取返回的数据
     * @param
     * @return
     */
    private String returnPath(String filePath,String fileName,String bucketName,String rootPath,String regionName){
        if(0==cosConfig.getReturnType()){
            return filePath;
        }
        Map<String,String> args=new HashMap<>();
        args.put("filePath",filePath);
        args.put("fileName",fileName);
        args.put("regionName",regionName);
        args.put("bucketName",bucketName);
        if(StringUtils.hasLength(rootPath))
            args.put("rootPath",rootPath);

        return StorageUtils.stringFormat(cosConfig.getReturnPath(),args);
    }

}
