package com.zhong.utils;

import java.io.*;

/**
 * Simple serialization/deserialization demonstrator.
 *
 * @author Dustin
 */
public class SerializationDemonstrator {
    /**
     * Serialize the provided object to the file of the provided name.
     *
     * @param objectToSerialize
     *         Object that is to be serialized to file; it is best that this
     *         object have an individually overridden toString()
     *         implementation as that is used by this method for writing our
     *         status.
     * @param fileName
     *         *         Name of file to which object is to be serialized.
     * @param <T>
     *         类的类型
     */
    public static <T> void serialize(final T objectToSerialize, final String fileName) {
        if (fileName == null) {
            throw new IllegalArgumentException("Name of file to which to serialize object to cannot be null.");
        }
        if (objectToSerialize == null) {
            throw new IllegalArgumentException("Object to be serialized cannot be null.");
        }
        try (FileOutputStream fos = new FileOutputStream(fileName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(objectToSerialize);
            //out.println("Serialization of Object " + objectToSerialize + " completed.");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    /**
     * Provides an object deserialized from the file indicated by the provided
     * file name.
     *
     * @param fileToDeserialize
     *         ame of file from which object is to be deserialized.
     * @param <T>
     *         类的类型
     *
     * @return 对象
     */
    public static <T> T deserialize(final String fileToDeserialize) {
        if (fileToDeserialize == null) {
            throw new IllegalArgumentException("Cannot deserialize from a null filename.");
        }

        T objectOut = null;
        try (FileInputStream fis = new FileInputStream(fileToDeserialize);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            objectOut = (T) ois.readObject();
            //out.println("Deserialization of Object " + objectOut + " is completed.");
        } catch (IOException | ClassNotFoundException exception) {
            exception.printStackTrace();
        }
        return objectOut;
    }
}