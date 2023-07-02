package com.toyota.cvqsfinal.utility;

import com.toyota.cvqsfinal.entity.DefectLocation;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;



@Component
public class ImageOperations {


    /**
     * Mark image with defect location
     * @param data - Image (byte array)
     * @param locationList - Defect location list
     * @return byte[] - Image (byte array)
     * @throws IOException - IOException
     */
    public byte[] markImage(byte[] data, List<DefectLocation> locationList) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(data);
        BufferedImage image = ImageIO.read(inputStream);
        int thickness = 6;
        int size = 30;


        //Green Mark
        Graphics2D graphics = image.createGraphics();
        graphics.setColor(Color.GREEN);
        graphics.setStroke(new BasicStroke(thickness));
        for (DefectLocation defectLocation : locationList) {
            int x = Integer.parseInt(defectLocation.getCoordX());
            int y = Integer.parseInt(defectLocation.getCoordY());
            graphics.drawLine(x - size, y - size, x + size, y + size);
            graphics.drawLine(x + size, y - size, x - size, y + size);
        }
        //Magenta Mark
        size = 20;
        thickness = 4;
        Graphics2D graphics2 = image.createGraphics();
        graphics2.setColor(Color.magenta);
        graphics2.setStroke(new BasicStroke(thickness));
        for (DefectLocation defectLocation : locationList) {
            int x = Integer.parseInt(defectLocation.getCoordX());
            int y = Integer.parseInt(defectLocation.getCoordY());
            graphics2.drawLine(x - size, y - size, x + size, y + size);
            graphics2.drawLine(x + size, y - size, x - size, y + size);
        }


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", baos);
        return baos.toByteArray();
    }


}
