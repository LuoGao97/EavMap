package com.geo;


import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.decoder.RESTCoverageStore;
import it.geosolutions.geoserver.rest.encoder.datastore.GSGeoTIFFDatastoreEncoder;

import java.io.File;
import java.net.URL;
import java.util.List;

public class TestGeoServer {
    public static void main(String[] args) throws Exception {
        //GeoServer的连接配置
        String url = "http://localhost:8080/geoserver";
        String username = "admin";
        String passwd = "geoserver";

        String workspace = "test";     //待创建和发布图层的工作区名称workspace
        String store_name = "testTiff"; //待创建和发布图层的数据存储名称store

        //判断工作区（workspace）是否存在，不存在则创建
        URL u = new URL(url);
        GeoServerRESTManager manager = new GeoServerRESTManager(u, username, passwd);
        GeoServerRESTPublisher publisher = manager.getPublisher();
        List<String> workspaces = manager.getReader().getWorkspaceNames();
        if (!workspaces.contains(workspace)) {
            boolean createws = publisher.createWorkspace(workspace);
            System.out.println("create ws : " + createws);
        } else {
            System.out.println("workspace已经存在了,workspace :" + workspace);
        }

        //判断数据存储（datastore）是否已经存在，不存在则创建
        String fileName = "D:/1.tif";

        RESTCoverageStore restStore = manager.getReader().getCoverageStore(workspace, store_name);
        if (restStore == null) {
            GSGeoTIFFDatastoreEncoder gsGeoTIFFDatastoreEncoder = new GSGeoTIFFDatastoreEncoder(store_name);
            gsGeoTIFFDatastoreEncoder.setWorkspaceName("mxleimm");
            gsGeoTIFFDatastoreEncoder.setUrl(new URL("file:" + fileName));
            boolean createStore = manager.getStoreManager().create(workspace, gsGeoTIFFDatastoreEncoder);
            System.out.println("create store (TIFF文件创建状态) : " + createStore);
            boolean publish = manager.getPublisher().publishGeoTIFF(workspace, store_name, new File(fileName));
            System.out.println("publish (TIFF文件发布状态) : " + publish);
        } else {
            System.out.println("数据存储已经存在了,store:" + store_name);
        }
    }
}