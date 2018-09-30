package com.luxu.classloader;

import com.sun.nio.zipfs.ZipInfo;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class PathClassLoader extends ClassLoader
{
    private String classPath;

    public PathClassLoader(String classPath)
    {
        this.classPath = classPath;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException
    {
        byte[] classData = getData(name);
        if (classData == null)
        {
            throw new ClassNotFoundException();
        }
        else
        {
            return defineClass(name, classData, 0, classData.length);
        }
    }

    private byte[] getData(String className)
    {
        String path = classPath + File.separatorChar+className.replace('.', File.separatorChar)+".class";
        try
        {
            InputStream is = new FileInputStream(path);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int num = 0;
            while((num = is.read(buffer))!=-1)
            {
                stream.write(buffer,0,num);
            }
            return stream.toByteArray();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve)
            throws ClassNotFoundException{
        Class<?> c = findClass(name);
        return c;
    }

    public static void main(String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException
    {
//        PathClassLoader pcl = new PathClassLoader("D:\\Users\\xulu\\IdeaProjects\\leetcode\\target\\classes");
//        Class c = pcl.loadClass("com.luxu.classloader.Test");
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        Class c = Class.forName(ZipInfo.class.getName(),false,cl);
        System.out.println(cl);
        System.out.println(c.getClassLoader());
    }
}