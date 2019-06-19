package com.example.tokobuku.server;

//This class is for storing all URLs as a model of URLs

public class Config_URL
{
    public static String base_URL           = "http://192.168.43.156:8080";
    public static String login              = base_URL + "/login";
    public static String register           = base_URL + "/registrasi";

    public static String inputBuku          = base_URL + "/inputbuku";
    public static String dataBuku           = base_URL + "/databuku";
    public static String updateBuku         = base_URL + "/updatebuku";
    public static String hapusBuku          = base_URL + "/hapusbuku";
}