/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopifyanalyzer;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.NotFound;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 *
 * @author ArrDogg
 */
public class Store {
    private String storeName;
    private String url;
    private String bestSellersPage;
    private List<Element> images;
    private String imagePath;
    
    
    
    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public List<Element> getImages() {
        return images;
    }

    public String getBestSellersPage() {
        return bestSellersPage;
    }

    public void setBestSellersPage(String bestSellersPage) {
        this.bestSellersPage = bestSellersPage;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String name) {
        this.imagePath = "C:\\Users\\ArrDogg\\Pictures\\ShopifyImages\\" + name;
    }
    
    

    public Store(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    public void setImages(List<Element> images) {
        this.images = images;
    }
    
    public void createImageDirectory(String directory) {
        String directoryPath = directory;
        File file = new File(directoryPath);
        file.setExecutable(true);
        file.setReadable(true);
        file.setWritable(true);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
    }
    
    public void downloadImages(Store store) throws NotFound, MalformedURLException, FileNotFoundException, IOException {
        for (int i = 0; i < store.getImages().size(); ++i) {
            String imageName = "\\image_" + i + ".jpg";
            
            URL url = new URL(this.images.get(i).getAt("src"));
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1!=(n=in.read(buf)))
            {
               out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            
            String imagePath = this.imagePath;
            imagePath += imageName;
            FileOutputStream fos = new FileOutputStream(imagePath);
            fos.write(response);
            fos.close();

        }
    }
}
