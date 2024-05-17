package com.suron.ysyliving.commodity.controller;

import com.aliyun.oss.*;
import com.suron.common.utils.R;
import com.suron.ysyliving.commodity.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author ysy
 * @version 1.0
 */
@RestController
@Slf4j
public class TestController {

    //编写方法-上传指定的文件到指定bucket [原生SDK完成文件上传到阿里云的 bucket]
    @RequestMapping("/test")
    public R testUpload() throws FileNotFoundException {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "oss-cn-beijing.aliyuncs.com";
        // 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tNU4ra8uX6rFoECZGTD";
        String accessKeySecret = "W0zm18B1nhaFZT5mFiJK8hhF99kAKL";
        // 填写Bucket名称，例如examplebucket。
        String bucketName = "hspliving-10003";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        // 文件上传后的文件名
        String objectName = "2022-12-12/12-hsp.jpg";
        // 填写本地文件的完整路径，例如D:\\localpath\\examplefile.txt。
        // 如果未指定本地路径，则默认从示例程序所属项目对应本地路径中上传文件流。
        // 这里指定你要上传的文件完整的路径
        String filePath = "C:\\Users\\Administrator\\Pictures\\Saved Pictures\\12.jpg";

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            InputStream inputStream = new FileInputStream(filePath);
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, inputStream);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
        return null;
    }


    //装配OSSClient
    @Resource
    private OSSClient ossClient;

    // 上传指定的文件到bucket
    @RequestMapping("/test2")
    public R testUpload2() throws FileNotFoundException {

        InputStream inputStream =
                new FileInputStream("C:\\Users\\Administrator\\Pictures\\Saved Pictures\\2.jpg");
        ossClient.putObject("hspliving-10003","2.jpg",inputStream);
        ossClient.shutdown();
        return R.ok("上传OK");
    }

    // 编写方法测试 getCascadedCategoryId
    // 传入的categoryId = 301 => [1,21,301]

    // 装配 CategoryService
    @Resource
    private CategoryService categoryService;
    @RequestMapping("/test3")
    public R testGetCascadedCategoryId() {
        Long[] cascadedCategoryId =
                categoryService.getCascadedCategoryId(301L);

        return R.ok().put("data", cascadedCategoryId);
    }
}
