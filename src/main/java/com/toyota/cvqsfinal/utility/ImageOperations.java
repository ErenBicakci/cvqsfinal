package com.toyota.cvqsfinal.utility;


import com.toyota.cvqsfinal.entity.DefectLocation;
import com.toyota.cvqsfinal.entity.Image;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


import javax.imageio.ImageIO;
import java.awt.*;
import java.util.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;


@Component
@RequiredArgsConstructor
public class ImageOperations {
    public Image markImage(Image image, List<DefectLocation> locations)throws Exception{

        InputStream inputStream = new ByteArrayInputStream(image.getData());
        BufferedImage image2 = ImageIO.read(inputStream);

        int size = 30;
        int thickness = 5;
        Graphics2D graphics = image2.createGraphics();
        graphics.setColor(Color.RED);
        graphics.setStroke(new BasicStroke(thickness));
        for (int i = 0;i < locations.size();i++){
            int x = Integer.parseInt(locations.get(i).getCoordX());
            int y = Integer.parseInt(locations.get(i).getCoordY());
            graphics.drawLine(x-size, y-size, x+size, y+size);
            graphics.drawLine(x+size, y-size, x-size, y+size);

        }
        graphics.dispose();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image2, "jpg", baos);
        byte[] imageInByte = baos.toByteArray();
        image.setData(imageInByte);
        return image;
    }
}
