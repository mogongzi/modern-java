package me.ryan.java.examples;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;

public class NIOExample {

    private static final Logger logger = LogManager.getLogger(NIOExample.class);

    public static void main(String[] args) throws IOException {
        logger.info("--------Debug---------");
        String textFile = Objects.requireNonNull(NIOExample.class.getClassLoader().getResource("nio-data.txt")).getPath();
        try (RandomAccessFile file = new RandomAccessFile(textFile, "r")) {
            FileChannel inChannel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int bytesRead = inChannel.read(buffer);
            while (bytesRead != -1) {
                logger.info(String.format("Read %s", bytesRead));
                buffer.flip();

                while(buffer.hasRemaining()) {
                    logger.info(String.valueOf((char)buffer.get()));
                }

                buffer.clear();
                bytesRead = inChannel.read(buffer);
            }
        }
    }
}
