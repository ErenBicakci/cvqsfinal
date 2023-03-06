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
     *
     * @param data - Resim (byte dizisi)
     * @param locationList - DefectLocation listesi
     * @return byte[] - Resim (byte dizisi)
     * @throws IOException
     */
    public byte[] markImage(byte[] data, List<DefectLocation> locationList) throws IOException {
        InputStream inputStream = new ByteArrayInputStream(data);
        BufferedImage image2 = ImageIO.read(inputStream);

        int size = 30;
        int thickness = 5;
        Graphics2D graphics = image2.createGraphics();
        graphics.setColor(Color.RED);
        graphics.setStroke(new BasicStroke(thickness));
        for (int i = 0;i < locationList.size();i++){
            int x = Integer.parseInt(locationList.get(i).getCoordX());
            int y = Integer.parseInt(locationList.get(i).getCoordY());
            graphics.drawLine(x-size, y-size, x+size, y+size);
            graphics.drawLine(x+size, y-size, x-size, y+size);
        }
        graphics.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image2, "jpg", baos);
        byte[] imageInByte = baos.toByteArray();
        return imageInByte;
    }
}
