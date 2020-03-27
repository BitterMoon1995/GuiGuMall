package com.lewo.zmail.manage.controller;

import com.lewo.zmail.manage.Manage_Web;
import org.csource.common.MyException;
import org.csource.fastdfs.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Objects;

//文件上传测试nigger
//@RunWith(SpringRunner.class)
//@SpringBootTest(classes = Manage_Web.class)
public class Manage_WebTest {
    public static void main(String[] args) throws IOException, MyException {
        String path = Manage_WebTest.class.getClassLoader().getResource("trackerConf").getPath();
//        System.out.println(path);
        ClientGlobal.init("trackerConf");

        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getTrackerServer();

        StorageServer storageServer = trackerClient.getStoreStorage(trackerServer);
        StorageClient storageClient = new StorageClient(trackerServer, storageServer);
        String[] uploadInfo = storageClient.upload_file("D:\\软件\\取色器\\eadc5ff2ly1gd4v7fzk5tj21h40u00vb.jpg", ".jpg", null);
        String url="http://192.168.156.128/";
        for (String s : uploadInfo) {
            url+="/"+s;
        }
        System.out.printf(url);
    }
}
