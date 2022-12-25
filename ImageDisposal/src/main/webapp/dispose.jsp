<%--
  Created by IntelliJ IDEA.
  User: zhang
  Date: 2022/9/6
  Time: 14:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page import="com.zzwarn.disposals.*"%>
<%@ page import="com.zzwarn.options.*" %>
<%@ page import="java.util.*" %>
<%@ page import="java.io.File" %>
<%@ page import="javax.print.DocFlavor" %>
<%@ page import="java.nio.file.Path" %>
<%@ page import="java.nio.file.Paths" %>
<%@ page import="java.net.URISyntaxException" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Disposal</title>
</head>
<body>
<%!
    String REC_PICS = "";
    String DISPOSAL_PICS = "";
%>
<%
    try{
        Path path = Paths.get(this.getClass().getResource("/").toURI());
        String root = path.getParent().getParent().toString();
        String[] dirs = new String[]{root, "img", "rec"};
        REC_PICS = String.join(File.separator, dirs);
        String[] dirs2 = new String[]{root, "img", "disposed"};
        DISPOSAL_PICS = String.join(File.separator, dirs2);
        File rec_folder = new File(REC_PICS);
        File dis_folder = new File(DISPOSAL_PICS);
        if (!rec_folder.exists()){
            boolean result_rec = rec_folder.mkdirs();
        }
        if (!dis_folder.exists()){
            boolean result_dis = dis_folder.mkdirs();
        }

    }catch(URISyntaxException e){
        System.out.println(e.toString());
    }
%>
<%
    System.out.println(request.toString());
    String imageStr = request.getParameter("image");
    System.out.println(imageStr.isEmpty());
    String cmd = request.getParameter("cmd");
    System.out.println(cmd);
    String[] prams = cmd.split("@");
    String filename = new String(prams[0]);
    int TYPE = Integer.parseInt(prams[1]);
    System.out.println(TYPE);
    String str_save_path = REC_PICS + File.separator + filename + ".jpg";
    DecodeImage.cvtStr2Img(imageStr, str_save_path);

    String[] dirs = new String[]{REC_PICS, filename +".jpg"};
    String img_path = String.join(File.separator, dirs);
    String dir_save = DISPOSAL_PICS;

    try {
        switch (TYPE) {
            case 0:
                // Binary
                Binarize.dispose(img_path, dir_save);
                break;
            case 1:
                // Cartoon
                Cartoon.dispose(img_path, dir_save);
                break;
            case 2:
                // Concave
                Concave.dispose(img_path, dir_save);
                break;
            case 3:
                // Convex
                Convex.dispose(img_path, dir_save);
                break;
            case 4:
                // Emboss
                Emboss.dispose(img_path, dir_save);
                break;
            case 5:
                // Frosted Glass
                FrostedGlass.dispose(img_path, dir_save);
                break;
            case 6:
                // Look-up Table
                String lut = new String(prams[2]);
                String lut_filename = lut + new String(".png");
                Lut.dispose(img_path, dir_save, lut_filename);
                break;
            case 7:
                // Nostalgia
                Nostalgia.dispose(img_path, dir_save);
                break;
            case 8:
                Pixelate.dispose(img_path, dir_save);
                break;
            case 9:
                // Segmentation
                String k = prams[2];
                Segment.dispose(img_path, dir_save, k);
                break;
            case 10:
                // Sharpen
                Sharpen.dispose(img_path, dir_save);
                break;
            default:
                break;
        }
    }catch (InterruptedException e){
        System.out.println(e.toString());
    }
%>
</body>
</html>
