/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shopifyanalyzer;

import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.NotFound;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author ArrDogg
 */
public class ShopifyAnalyzer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NotFound, FileNotFoundException, IOException {
        // TODO code application logic here
        
        
        try{
            
            // Ask user what keywaord they would like to use
            Scanner scanner = new Scanner(System.in);
            System.out.println("Please enter a keyword to analyze: ");
            String keyword = scanner.nextLine();
            
            System.out.println("Please wait while we analyze your keyword. This may take a few minutes...");
            
            
            // Scrape google search results for shopify stores with specified keyword
            List<Element> linkList = findStores(keyword);
            
            // Determine how many links are actually shopify links
            int numOfStores = 0;
            for (int i = 0; i < linkList.size(); ++i) {
                String storeUrl = formatStoreUrl(linkList.get(i));
                if (storeUrl.contains("shopify")) {
                    ++numOfStores;
                }
                
            }
            
            // Create array for Store objects
            Store[] stores = new Store[numOfStores];
            
            // Create Store objects for all shopify stores and place them in store array
            for (int i = 0; i < linkList.size(); ++i) {
                if (i >= numOfStores) {
                    
                } else {
                    String storeUrl = formatStoreUrl(linkList.get(i));
                    if (storeUrl.contains("shopify")) {
                        stores[i] = new Store(storeUrl);
                        String url = stores[i].getUrl();
                        stores[i].setStoreName(formatStoreName(url));
                        stores[i].setImagePath(stores[i].getStoreName());
                        stores[i].setBestSellersPage(bestSellerPage(url));
                    }
                }
                
            }

            // Set best seller page for all stores
            for (int i = 0; i < stores.length; ++i) {
                if (stores[i] == null) {
                    
                } else {
                    String url = stores[i].getUrl();
                    stores[i].setBestSellersPage(bestSellerPage(url));
                    stores[i].setImages(imageUrls(stores[i].getBestSellersPage()));
                }
            }
            
            // Set image elements for images on best seller page
            if (stores[0] != null) {
                stores[0].setImages(imageUrls(stores[0].getBestSellersPage()));
            }
            
            
            // For each image element, find url
            System.out.println("\nNow downloading images for reverse image searches...");
            for (int i = 0; i < stores.length; ++i) {
                if (stores[i] != null) {
                    stores[i].createImageDirectory(stores[i].getImagePath());
                    stores[i].downloadImages(stores[i]);
                }
            }
            
            System.out.println("\nDownloading done. Now ready to reverse image search!");
                        
            /*String store = formatStoreUrl(linkList.get(6));
            
        
            Store store1 = new Store(store);
            System.out.println(visitStore(bestSellerPage(store1.getUrl())));
            
            System.out.println(imageUrls(bestSellerPage(store1.getUrl())).get(2).getAt("src"));
            
            /*userAgent.visit(store);
            System.out.println(userAgent.doc.innerHTML());*/
        
          
        
        // Need to parse into substrings by equal sign, then 
        }
        catch(JauntException e){         //if an HTTP/connection error occurs, handle JauntException.
            System.err.println(e);
        }
        
    }
    
    static String formatStoreUrl(Element url) throws NotFound {
        String[] storeToSplit = url.getAt("href").split("=");
        String[] storeToSplitAgain = storeToSplit[1].split("&");
        String[] storeMainPage = storeToSplitAgain[0].split(".com");
        String store = storeMainPage[0] + ".com";
        
        return store;
    }
    
    static String formatStoreName(String url) {
        String[] urlToSplit = url.split("/");
        return urlToSplit[2];
    }
    
    static String bestSellerPage(String url) {
        String bestSellerPage = url + "/collections/all?sort_by=best-selling&page=1";
        return bestSellerPage;
    }
    
    static List<Element> findStores(String keyword) throws JauntException {
        String keywordSearch = keyword + " site:myshopify.com";
        
        UserAgent userAgent = new UserAgent();
        userAgent.visit("http://google.com");
        userAgent.doc.apply(keywordSearch).submit();
        
        Elements links = userAgent.doc.findEvery("<h3>").findEvery("<a>");  
        List<Element> linkList = links.toList();
        
        return linkList;
    }
    
    static String visitStore(String url) throws ResponseException {
        
        UserAgent userAgent = new UserAgent();
        userAgent.visit(url);
        System.out.println("Visiting " + url);
        return userAgent.doc.innerHTML();
        
    }
    
    static List<Element> imageUrls(String url) throws ResponseException {
        UserAgent userAgent = new UserAgent();
        userAgent.visit(url);
        Elements imageLinks = userAgent.doc.findEvery("<img src>");
        List<Element> linkList = imageLinks.toList();
        
        return linkList;
    }
    
}
