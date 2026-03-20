package com.juyou.common.exceluntils;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import com.juyou.common.env.EnvKey;
import com.juyou.common.env.EnvUtils;
import com.juyou.common.error.ErrorCode;
import com.juyou.common.exception.BaseException;
import com.juyou.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;


/**
 * 导出excel工具类
 *
 * @author kaedeliu
 */
@Component
public class ExcelUtils {

	@Autowired
	EnvUtils envUtils;



	/**
	 *
	 * @Title: exportExcel
	 * @Description: 导出excel
	 * @param list  导出的数据
	 * @param title  文件标题
	 * @param sheetName  sheet名称
	 * @param pojoClass  集合的类
	 * @param fileName   文件名
	 * @param response
	 * @return void
	 */
	public static void exportExcel(List<?> list, String title, String sheetName, Class<?> pojoClass, String fileName, HttpServletResponse response) {
		Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(title, sheetName), pojoClass, list);
		if (workbook != null) {
			try {
				response.setCharacterEncoding("UTF-8");
				response.setHeader("content-Type", "application/vnd.ms-excel");
				response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes("UTF-8"), "ISO-8859-1"));
				workbook.write(response.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @功能 文件方式导出数据
	 * @Author kaedeliu
	 * @创建时间 2026/3/18 10:49
	 * @修改人 kaedeliu
	 * @修改时间 2026/3/18 10:49
	 * @Param
	 * @param list:
	 * @param biaoName:
	 * @param tClass:
	 * @return
	 **/
	public <T> String listExcel(List<T> list, String biaoName, Class<T> tClass) throws IOException {
		return exportExcel(null, null, list, biaoName, tClass);
	}

	/**
	 * @功能 文件方式导出数据
	 * @Author kaedeliu
	 * @创建时间 2026/3/18 10:49
	 * @修改人 kaedeliu
	 * @修改时间 2026/3/18 10:49
	 * @Param
	 * @param list:
	 * @param biaoName:
	 * @param tClass:
	 * @return
	 **/
	public <T> String exportExcel(String title, String sheetName, List<T> list, String biaoName, Class<T> tClass) throws IOException {
		if (list.isEmpty()) {
			throw new BaseException(ErrorCode.A9200, "未查询到信息导出失败");
		}
		final Integer msgType = new Random().nextInt(Integer.MAX_VALUE);

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
//				String fileName = null;
//				String msg = null;
//				try {
//					fileName = createExcel(title, sheetName, list, biaoName, tClass);
//				} catch (IOException e) {
//					e.printStackTrace();
//					msg = e.getMessage();
//				} finally {
//					if (msg == null)
//						CmsgUtils.addMsg(macSeq, msgType, StatusEnum.前端消息状态_完成.getCode(), fileName);
//					else {
//						CmsgUtils.addMsg(macSeq, msgType, StatusEnum.前端消息状态_错误.getCode(), msg);
//						CmsgUtils.addMsg(macSeq, msgType, StatusEnum.前端消息状态_完成.getCode(), null);
//					}
//				}
			}
		});
		thread.setName("export-thread-1");
		thread.start();
		return msgType + "";
	}

	private <T> String createExcel(String title, String sheetName, List<T> list, String biaoName, Class<T> tClass) throws IOException {
		// 创建参数对象（用来设定excel得sheet的内容等信息）
		ExportParams params = new ExportParams(title, sheetName); // sheet
		params.setStyle(NewExcelExportStylerDefaultImpl.class);
		long t = System.currentTimeMillis();
		Workbook workbook = ExcelExportUtil.exportExcel(params, tClass, list);
		File file = getExportFile(biaoName);
		FileOutputStream fos = new FileOutputStream(file);
		try {
			workbook.write(fos);
			workbook.close();
			System.out.println("创建耗时:" + (System.currentTimeMillis() - t));
		} finally {
			fos.close();
		}
		return file.getName();
	}

//    public  Workbook exportBigExcel( ExportParams params,List<T> list, String biaoName, Class<T> tClass) {
//        if (list.isEmpty()) {
//            throw new BaseException(ErrorCode.A9200, "未查询到信息导出失败");
//        }
//        // 创建参数对象（用来设定excel得sheet的内容等信息）
//        ExportParams params = new ExportParams(); //sheet
//        params.setStyle(NewExcelExportStylerDefaultImpl.class);
//        // title的参数为ExportParams类型，目前仅仅在ExportParams中设置了sheetName
//        long t=System.currentTimeMillis();
//        Workbook workbook = ExcelExportUtil.exportBigExcel(params,tClass,list);
//    }

	/**
	 * @功能 获取输出路径
	 * @Author kaedeliu
	 * @创建时间 2026/3/18 10:49
	 * @修改人 kaedeliu
	 * @修改时间 2026/3/18 10:49
	 * @Param
	 * @param fileName:
	 * @return
	 **/
	public File getExportFile(String fileName) {
		String endName = DateUtils.formatDate(new Date(), "yyyyMMddHHmmsss");
		String endBuf = "";
		String fname = "";
		if (fileName == null)
			fileName = "";
		else if (fileName.indexOf(".") > 0) {
			fname = fileName.substring(0, fileName.indexOf("."));
			endBuf = fileName.substring(fileName.indexOf("."));
		}
		String endFileName = fname + endName + endBuf;
		String filePath = envUtils.value(EnvKey.导出文件存放地址);
		if (StringUtils.isEmpty(filePath)) {
			throw new BaseException(ErrorCode.B0303, "路径配置异常");
		}
		File file = new File(filePath, endFileName);
		if (!file.getParentFile().exists()) {
			file.getParentFile().mkdirs();
		}
		return file;
	}

	/**
	 * 导入文件解析
	 * @param file
	 * @param classzz
	 * @param <T>
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> importExcel(MultipartFile file,Class<T> classzz) throws Exception {
		ImportParams params = new ImportParams();
		params.setNeedVerify(true);
//		params.setVerifyGroup(new Class[]{classzz});
//		long start = new Date().getTime();
		ExcelImportResult<T> result = ExcelImportUtil.importExcelMore(
				file.getInputStream(),
				classzz, params);
		return result.getList();
	}

}
