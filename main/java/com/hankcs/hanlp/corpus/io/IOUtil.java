/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/9/8 23:04</create-date>
 *
 * <copyright file="Util.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014, 上海林原信息科技有限公司. All Right Reserved, http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.corpus.io;


import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import static com.hankcs.hanlp.utility.Predefine.logger;

/**
 * 一些常用的IO操作
 *
 * @author hankcs
 */
public class IOUtil
{
    /**
     * 序列化对象
     *
     * @param o
     * @param path
     * @return
     */
    public static boolean saveObjectTo(Object o, String path)
    {
        try
        {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
            oos.writeObject(o);
            oos.close();
        }
        catch (IOException e)
        {
            logger.warning("在保存对象" + o + "到" + path + "时发生异常" + e);
            return false;
        }

        return true;
    }

    /**
     * 反序列化对象
     *
     * @param path
     * @return
     */
    public static Object readObjectFrom(String path)
    {
        ObjectInputStream ois = null;
        try
        {
            ois = new ObjectInputStream(new FileInputStream(path));
            Object o = ois.readObject();
            ois.close();
            return o;
        }
        catch (IOException | ClassNotFoundException e)
        {
            logger.warning("在从" + path + "读取对象时发生异常" + e);
        }

        return null;
    }

    /**
     * 一次性读入纯文本
     *
     * @param path
     * @return
     */
    public static String readTxt(String path)
    {
        if (path == null) return null;
        File file = new File(path);
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try
        {
            FileInputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
        }
        catch (FileNotFoundException e)
        {
            logger.warning("找不到" + path + e);
            return null;
        }
        catch (IOException e)
        {
            logger.warning("读取" + path + "发生IO异常" + e);
            return null;
        }

        return new String(fileContent);
    }

    /**
     * 快速保存
     * @param path
     * @param content
     * @return
     */
    public static boolean saveTxt(String path, String content)
    {
        try
        {
            FileChannel fc = new FileOutputStream(path).getChannel();
            fc.write(ByteBuffer.wrap(content.getBytes()));
            fc.close();
        }
        catch (Exception e)
        {
            logger.throwing("IOUtil", "saveTxt", e);
            return false;
        }
        return true;
    }

    /**
     * 将整个文件读取为字节数组
     *
     * @param path
     * @return
     */
    public static byte[] readBytes(String path)
    {
        try
        {
            FileInputStream fis = new FileInputStream(path);
            FileChannel channel = fis.getChannel();
            int fileSize = (int) channel.size();
            ByteBuffer byteBuffer = ByteBuffer.allocate(fileSize);
            channel.read(byteBuffer);
            byteBuffer.flip();
            byte[] bytes = byteBuffer.array();
            byteBuffer.clear();
            channel.close();
            fis.close();
            return bytes;
        }
        catch (Exception e)
        {
            logger.warning("读取" + path + "时发生异常" + e);
        }

        return null;
    }
}
