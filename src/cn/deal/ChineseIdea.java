package cn.deal;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by Administrator on 2017/5/7.
 */
public class ChineseIdea {
    /**
     * 保存配置文件
     * @param filePath 保存路径
     * @param pro 配置信息Properties
     */
    public static void storPropertyFile(String filePath,Properties pro) throws Exception{
        OutputStream out = new FileOutputStream(filePath);
        pro.store(out,"");
        out.close();
    }

    /**
     * 更新配置文件
     * @param cnFile 中文配置文件
     * @param enFile 英文配置文件
     * @return Properties 配置类
     * @throws Exception 异常
     */
    public static Properties updateProFile(File cnFile,File enFile) throws Exception{
        Properties cnPro = new Properties();
        cnPro.load(new FileInputStream(cnFile));
        Properties enPro = new Properties();
        enPro.load(new FileInputStream(enFile));
        Enumeration keys = enPro.propertyNames();
        while (keys.hasMoreElements()){
            String strKey = (String) keys.nextElement();
            String cnValue = cnPro.getProperty(strKey);
            if(cnValue!=null&&!cnValue.equals("")){
                enPro.setProperty(strKey,dealValue(strKey,cnValue,enPro.getProperty(strKey)));
            }
        }
        return enPro;
    }

    /**
     *
     * 处理快捷键(_X)和命令(&)
     * @param key 配置文件属性
     * @param valCn 中文属性值
     * @param valEn 英文属性值
     * @return 处理后的值
     */
    public static String dealValue(String key,String valCn,String valEn){

        //值不相同
        if(!valCn.equals(valEn)){
            //快捷键处理
            int en_index = valEn.indexOf("_");
            if(en_index!=-1){
                if(countStr(valEn,"_")>1){
                    System.out.println("_个数过多，已替换为英文，key="+key+"\n英文："+valEn+"\n中文："+valCn);
                    return valEn;
                }else{
                    String quStr = valEn.substring(en_index,en_index+2);
                    int cn_index = valCn.indexOf("(_");
                    if (cn_index!=-1){
                        if(!valCn.contains("("+quStr+")")){
                            //System.out.println("_替换，"+enFile.getName()+"->>lineNum:"+nrEn.getLineNumber());
                            valCn = valCn.replaceAll("\\(_.\\)","("+quStr+")");
                        }
                    }else {
                        valCn = valCn + " ("+quStr+")";
                    }
                }
            }
            //命令处理
            int en_andIndex = valEn.indexOf("&");
            if(en_andIndex!=-1){
                if(countStr(valEn,"&")>1){
                    System.out.println("&个数过多，已替换为英文，key="+key+"\n英文："+valEn+"\n中文："+valCn);
                    return valEn;
                }else{
                    String quStr = valEn.substring(en_andIndex,en_andIndex+2);
                    int cn_index = valCn.indexOf("(&");
                    if (cn_index!=-1){
                        if(!valCn.contains("("+quStr+")")){
                            //System.out.println("&替换，"+enFile.getName()+"->>lineNum:"+nrEn.getLineNumber());
                            valCn = valCn.replaceAll("\\(&.\\)","("+quStr+")");
                        }
                    }else {
                        valCn = valCn + " ("+quStr+")";
                    }
                }
            }
        }
        return valCn;

    }
    /**
     * 判断str1中包含str2的个数
     * @param str1
     * @param str2
     * @return counter
     */
    private static int countStr(String str1, String str2) {
        int count = 0;
        while (str1.indexOf(str2) != -1) {
            count++;
            str1 = str1.substring(str1.indexOf(str2) + str2.length());
        }
        return count;
    }


    public static void main(String[] args) throws Exception{
        String basePath = "/home/lihn/桌面";
        File cnDir = new File(basePath+"/messages_cn");
        File[] cnFiles = cnDir.listFiles();
        File enDir = new File(basePath+"/messages_en");
        File[] enFiles = enDir.listFiles();
        for (File cnf : cnFiles){
            String cnfName = cnf.getName();
            for(int i=0;i<enFiles.length;i++){
                String enfName = enFiles[i].getName();
                if(enfName.equals(cnfName)){
                    Properties pro = updateProFile(cnf,enFiles[i]);
                    storPropertyFile(basePath+"/messages/"+enfName,pro);
                }
            }
        }
    }
}