import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.util.JSON;
import net.sf.json.JSONObject;
import org.bson.BSONObject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;


public class getAssets {
    private static List<Asset> assets=null;

    public static List<Asset> getAssetModels(ArrayList<Document> documents) throws Exception {
        assets = new ArrayList<Asset>();
        //遍历books
        for (int i = 0; i < documents.size(); i++) {
            System.out.print("i is :"+i);
            Asset asset = new Asset();
            Document document=documents.get(i);

            String filename = document.get("Generated File Name").toString();
            asset.setFileName(filename);

            String title = document.get("Media Title").toString();
            asset.setTitle(title);

            String owner = document.get("Media Owner").toString();
            asset.setOwner(owner);

            String item_description = document.get("Item Description").toString();
            asset.setDescription(item_description);

//            String resolution = element.getElementsByTagName("resolution").item(0).getTextContent();
//            asset.setResolution(resolution);
            String resolution=document.get("File Dimensions").toString();
            asset.setResolution(resolution);

            String color_code =document.get("Color Mode").toString();
            asset.setColorCode(color_code);
            //copyright
            String remark = document.get("Remarks").toString();
            asset.setRights(remark);
            String[] modelArray={"S60","S90","V40","V40 Cross Country","V60","V60 Cross Country","V90","V90 Cross Country","XC40","XC60","XC90"};
            List<String> modelYears = new ArrayList<String>();
            List<String> categories = new ArrayList<String>();
            List<String> themes = new ArrayList<String>();
            List<String> models = new ArrayList<String>();
            String path = document.get("Full Theme Paths (comma-separated)").toString();
            String[] list = path.split("\\\\");
            if (path.contains("Media Type\\Images")) {
                if (path.contains("Exterior")) {
                    categories.add("Exterior");
                    if (path.contains("Exterior Colour")) {
//                        if (list.length > 0) {
//                            String color = list[list.length - 1];
//                            asset.setColor(color);
//                        }
                        for (int j = 0; j <list.length ; j++) {
                            if(list[j].equals("Exterior Colour")){
                                String color=list[j+1];
                                if(color.contains(",")){
                                    color=color.substring(0,list[j+1].indexOf(','));
                                }
                                asset.setColor(color);
                            }
                        }
                    }
                }
                if (path.contains("Interior")) {
                    categories.add("Interior");
                }
                if (path.contains("Accessories")) {
                    categories.add("Accessories");
                }
                if (path.contains("Illustration")) {
                    themes.add("Illustration");
                }
                if (path.contains("Wheel")) {
                    themes.add("Wheel");
                }
                if (path.contains("Outdoor")) {
                    asset.setLocation("Outdoor");
                }
                if (path.contains("Indoor")) {
                    asset.setLocation("Indoor");
                }
            }
            if (path.contains("Angles whole exterior")) {
                if (list.length > 0) {
//                    String angle = list[list.length - 1];
//                    asset.setAngle(angle);
                    for (int j = 0; j <list.length ; j++) {
                        if(list[j].equals("Angles whole exterior")){
                            String angle=list[j+1];
                            if(angle.contains(",")){
                                angle=angle.substring(0,list[j+1].indexOf(','));
                            }
                            asset.setAngle(angle);
                        }
                    }
                }
            }
            if (path.contains("Other Asset Categories")) {
                if (path.contains("Model Year")) {
                    if (list.length > 0) {
//                        String modelYear = list[list.length - 1];
//                        modelYears.add(modelYear);
                        for (int j = 0; j <list.length ; j++) {
                            if(list[j].equals("Model Year")){
//                                modelYears.add(list[j+1].substring(0,list[j+1].indexOf(',')));
                                String model=list[j+1];
                                if(model.contains(",")){
                                    model=model.substring(0,list[j+1].indexOf(','));
                                }
                                modelYears.add(model);
                            }
                        }
                    }
                }
            }
            if (path.contains("Other Asset Categories\\Lifestyle")) {

                themes.add("Lifestyle");
                if (path.contains("People")) {
                    themes.add("People");
                }
                if (path.contains("Man")) {
                    themes.add("Man");
                }
                if (path.contains("Women")) {
                    themes.add("Women");
                }
            }
            if (list.length > 1) {
//                    if( Arrays.asList(modelArray).contains(list[1])){
//                        models.add(list[1]);
//                    }else if( Arrays.asList(modelArray).contains(list[2])){
//                        models.add(list[2]);
//                    }
                for (int j = 0; j < list.length; j++) {
                    if (Arrays.asList(modelArray).contains(list[j])) {
                        models.add(list[j]);
                    }
                }
            }
            for (int j = 0; j <modelArray.length ; j++) {
                if(item_description.contains(modelArray[j])){
                    models.add(modelArray[j]);
                }
                if(title.contains(modelArray[j])){
                    models.add(modelArray[j]);
                }
            }
            asset.setTheme(removeDuplicate(themes));
            asset.setModelYear(removeDuplicate(modelYears));
            asset.setCategory(removeDuplicate(categories));
            asset.setModels(removeDuplicate(models));
            assets.add(asset);
            System.out.println("success");
        }


        return assets;

    }

    public static void main(String args[]){
        try {
            //连接数据库
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            MongoDatabase mongoDatabase=mongoClient.getDatabase("volvo");
            MongoCollection<Document> collection=mongoDatabase.getCollection("volvos");

            //查询条件
//            Bson filter=Filters.eq("Media ID","101589");
            //查询
            FindIterable<Document> findIterable=collection.find();
            //跌代
            MongoCursor cursor=findIterable.iterator();
            int num=0;
            ArrayList<Document> documents=new ArrayList<Document>();
            while(cursor.hasNext()){
                documents.add((Document) cursor.next()) ;
            }
            System.out.println(documents.size());
            List<Asset> list=getAssets.getAssetModels(documents);
            System.out.println(list.size());
//            listToExcel(list, "/content/dam/cm/en/public/models/v40/v40-cross-country/images/", "test");
            listToExcel(list, "/content/dam/cm/en/public/models/", "volvo");
        } catch (Exception e) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public static List removeDuplicate(List list) {
        if (list.size() > 0) {
            HashSet h = new HashSet(list);
            list.clear();
            list.addAll(h);
        }
        return list;
    }


    public static void listToExcel(List<Asset> assets, String path, String title) throws IOException {
        List<String> header = new ArrayList<String>();
        header.add("assetPath");
        header.add("dc:title{{String}}");
        header.add("volvo:usage{{String}}");
        header.add("volvo:color{{String}}");
        header.add("volvo:color-code{{String}}");
        header.add("volvo:model-year{{String: multi }}");
        header.add("volvo:location{{String}}");
        header.add("volvo:category{{String: multi }}");
        header.add("volvo:angle{{String}}");
        header.add("volvo:theme{{String: multi }}");
        header.add("dc:description{{String}}");
        header.add("dc:rights{{String}}");
        header.add("volvo:owner{{String}}");
        header.add("dc:language{{String}}");
        header.add("volvo:resolution{{String}}");
        header.add("volvo:model{{String: multi }}");
        header.add("volvo:recomended{{String}}");

        OutputStream out = new FileOutputStream("E:/" + title + ".xls");// 输出目的地
        //新建excel报表
        HSSFWorkbook excel = new HSSFWorkbook();
        //添加一个sheet
        HSSFSheet hssfSheet = excel.createSheet(title);
        //往excel表格创建一行，excel的行号是从0开始的
        // 设置表头
        HSSFRow firstRow = hssfSheet.createRow(0);
        for (int columnNum = 0; columnNum < header.size(); columnNum++) {
            //创建单元格
            HSSFCell hssfCell = firstRow.createCell(columnNum);
            //设置单元格的值
            hssfCell.setCellValue(header.size() < columnNum ? "-" : header.get(columnNum));
        }
        // 设置主体数据
        for (int rowNum = 0; rowNum < assets.size(); rowNum++) {
            //往excel表格创建一行，excel的行号是从0开始的
            HSSFRow hssfRow = hssfSheet.createRow(rowNum + 1);
            Asset asset = assets.get(rowNum);
            String model="";
            if(null != asset.getModels() && asset.getModels().size() > 0){
                model=asset.getModels().get(0);
                if(model.equals("V40")){
                    model="v40/v40";
                }else if(model.equals("V40 Cross Country")){
                    model="v40/v40-cross-country";
                }else if(model.equals("V60")){
                    model="v60/v60";
                }else if(model.equals("V60 Cross Country")){
                    model="v60/v60-cross-country";
                }else if(model.equals("V90")){
                    model="v90/v90";
                }else if(model.equals("V90 Cross Country")){
                    model="v90/v90-cross-country";
                }else{
                    model=model.toLowerCase();
                }
            }else{
                model="others";
            }
            String extension=asset.getFileName().substring(asset.getFileName().lastIndexOf('.')+1);
            String type="";
            if(extension.equals("tif")||extension.equals("ai")||
                    extension.equals("bmp")||extension.equals("eps")||
                    extension.equals("gif")||extension.equals("GIF")||
                    extension.equals("jpeg")||extension.equals("jpg")||
                    extension.equals("JPG")||extension.equals("psd")||
                    extension.equals("png")||extension.equals("PNG")||
                    extension.equals("TIF")){
                type="images";
            }else if(extension.equals("aiff")||extension.equals("flv")
                    ||extension.equals("mov")||extension.equals("mp3")||
                    extension.equals("mp4")||extension.equals("wmv")||
                    extension.equals("mpg")||extension.equals("mpeg")){
                type="films";
            }else if(extension.equals("dfont")||extension.equals("idml")||
                    extension.equals("indd")||extension.equals("otf")||extension.equals("srt")){
                type="printing-material";
            }else if(extension.equals("pps")||extension.equals("pot")||
                    extension.equals("ppt")||extension.equals("pptx")){
                type="ppt";
            }else{
                type="others";
            }
            String recomended="False";
            if(asset.getLocation()!=null){
                if(asset.getLocation().equals("Outdoor")){
                    recomended="True";
                }
            }
            hssfRow.createCell(0).setCellValue(path +model +"/"+type+"/"+asset.getFileName());
            hssfRow.createCell(1).setCellValue(asset.getTitle());
            hssfRow.createCell(2).setCellValue("Internal");
            hssfRow.createCell(3).setCellValue(asset.getColor());
            hssfRow.createCell(4).setCellValue(asset.getColorCode());
            hssfRow.createCell(5).setCellValue(multiValue(asset.getModelYear()));
            hssfRow.createCell(6).setCellValue(asset.getLocation());
            hssfRow.createCell(7).setCellValue(multiValue(asset.getCategory()));
            hssfRow.createCell(8).setCellValue(asset.getAngle());
            hssfRow.createCell(9).setCellValue(multiValue(asset.getTheme()));
            hssfRow.createCell(10).setCellValue(asset.getDescription());
            hssfRow.createCell(11).setCellValue(asset.getRights());
            hssfRow.createCell(12).setCellValue(asset.getOwner());
            hssfRow.createCell(13).setCellValue("en");
            hssfRow.createCell(14).setCellValue(asset.getResolution());
            hssfRow.createCell(15).setCellValue(multiValue(asset.getModels()).toUpperCase());
            hssfRow.createCell(16).setCellValue(recomended);
        }
        try {
            excel.write(out);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String multiValue(List<String> list) {
        String value = "";
        if (null != list && list.size() > 0) {
            if (list.size() == 1) {
                return list.get(0);
            } else {
                for (int i = 0; i < list.size() - 1; i++) {
                    value += list.get(i) + "|";
                }
                value += list.get(list.size() - 1);
            }
        }
        return value;
    }
}
