package com.juyou.storage.alioss;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.juyou.storage.config.OssConfig;
import com.juyou.storage.utils.StorageUtils;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @功能 处理阿里云OSS存储功能
 * @Author kaedeliu
 * @创建时间 2026/3/18 10:49
 * @修改人 kaedeliu
 * @修改时间 2026/3/18 10:49
 * @Param
 * @return
**/
@Component
public class AliOssUtils {


	@Autowired
	OssConfig ossConfig;

	public String upload(String filePath, InputStream inputStream) {
		return upload(filePath, null, inputStream);
	}


	private String upload(String filePath, byte[] md5, InputStream inputStream) {
		try {
			// 创建OSSClient实例。
			OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndpoint(),
					ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
			String path = ossConfig.getRootPath()+ File.separator+filePath;
			// 创建PutObjectRequest对象。
			// <yourObjectName>表示上传文件到OSS时需要指定包含文件后缀在内的完整路径，例如abc/efg/123.jpg。
			PutObjectRequest putObjectRequest = new PutObjectRequest(ossConfig.getBucketName(), filePath, inputStream);

			// 如果需要上传时设置存储类型与访问权限，请参考以下示例代码。
			if(md5!=null && md5.length>0) {
				ObjectMetadata metadata = new ObjectMetadata();
				String md5str=Base64.encodeBase64String(md5);
				metadata.setContentMD5(md5str);
				// metadata.setHeader(OSSHeaders.OSS_STORAGE_CLASS, StorageClass.Standard.toString());
				// metadata.setObjectAcl(CannedAccessControlList.Private);
				putObjectRequest.setMetadata(metadata);
			}
			// 上传字符串。
			ossClient.putObject(putObjectRequest);

			// 关闭OSSClient。
			ossClient.shutdown();
			return returnPath(path,filePath, ossConfig.getBucketName(), ossConfig.getRootPath());
		}catch(Exception e) {
			throw  e;
		}

	}

	/**
	 * 获取返回的数据
	 * @param
	 * @return
	 */
	private String returnPath(String filePath,String fileName,String bucketName,String rootPath){
		if(0==ossConfig.getReturnType()){
			return filePath;
		}
		Map<String,String> args=new HashMap<>();
		args.put("filePath",filePath);
		args.put("fileName",fileName);
		args.put("bucketName",bucketName);
		if(StringUtils.hasLength(rootPath))
			args.put("rootPath",rootPath);

		return StorageUtils.stringFormat(ossConfig.getReturnPath(),args);
	}

	/**
	 * 删除文件
	 * @param objectName
	 * @return
	 */
	public boolean delete(String objectName) {
		return delete(ossConfig.getBucketName(), objectName);
	}

	/**
	 * 删除文件
	 * @param bucketName
	 * @param objectName
	 * @return
	 */
	public boolean delete(String bucketName,String objectName) {
		try {

			OSS ossClient = new OSSClientBuilder().build(ossConfig.getBucketName(),
					ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());
			ossClient.deleteObject(bucketName, objectName);
			// 关闭OSSClient。
			ossClient.shutdown();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 得到临时URL
	 * @param objectName
	 * @param time
	 * @return
	 */
	public String signUrl(String objectName,long time ) {
		return signUrl(ossConfig.getBucketName(), objectName, time);
	}

	/**
	 * 临时的url
	 * @param bucketName
	 * @param objectName
	 * @param time
	 * @return
	 */
	public String signUrl(String bucketName,String objectName,long time ) {
		// 创建OSSClient实例。
		OSS ossClient = new OSSClientBuilder().build(ossConfig.getEndpoint(),
				ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret());

		// 设置URL过期时间为1小时。
		Date expiration = new Date(time);
		// 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
		URL url = ossClient.generatePresignedUrl(bucketName, objectName, expiration);
		// 关闭OSSClient。
		ossClient.shutdown();
		return url.toString();
	}
}
