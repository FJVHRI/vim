package com.campus.ui;

import com.campus.dao.ApplyDao;
import com.campus.util.QRUtil;
import org.openpdf.text.Document;
import org.openpdf.text.Element;
import org.openpdf.text.Font;
import org.openpdf.text.Chunk;
import org.openpdf.text.Paragraph;
import org.openpdf.text.Phrase;
import org.openpdf.text.Image;
import org.openpdf.text.PageSize;
import org.openpdf.text.pdf.PdfPTable;
import org.openpdf.text.pdf.PdfPCell;
import org.openpdf.text.pdf.PdfWriter;
import org.openpdf.text.pdf.BaseFont;

import javax.swing.JOptionPane;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class PdfTicket {
    // 时间格式化常量
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void generate(int stuId, int actId, String account) {
        // 严格按你的要求：仅声明SimHei.ttf相关字体变量
        BaseFont bfChinese = null;
        Font chinese = null;
        Font chineseBold = null;

        try {
            // ========== 仅使用你指定的SimHei.ttf字体 ==========
            // 注意：请确保SimHei.ttf文件放在项目根目录（和src同级）
            bfChinese = BaseFont.createFont("SimHei.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            chinese = new Font(bfChinese, 12, Font.NORMAL);       // 常规中文
            chineseBold = new Font(bfChinese, 20, Font.BOLD);     // 加粗中文（标题用）

        } catch (Exception e) {
            // 仅保留降级提示，不替换为其他字体
            System.out.println("SimHei.ttf字体加载失败：" + e.getMessage());
            JOptionPane.showMessageDialog(null, "请确认SimHei.ttf文件已放在项目根目录！");
            return; // 加载失败则终止，不使用其他字体
        }

        try {
            // 1. 查询报名信息
            Map<String, Object> info = new ApplyDao().getTicketInfo(stuId, actId);
            if (info == null) {
                JOptionPane.showMessageDialog(null, "未找到报名记录");
                return;
            }

            // 2. 创建目录+定义PDF文件名（使用你指定的account参数）
            Files.createDirectories(Paths.get("ticket"));
            String file = "ticket/act" + actId + "_" + account + ".pdf";
            System.out.println("生成PDF路径：" + file);

            // 3. 初始化PDF文档
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(file));
            doc.open();

            // ========== 按你的要求添加标题（使用chineseBold） ==========
            Paragraph title = new Paragraph("校园活动电子票", chineseBold);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(Chunk.NEWLINE);

            // ========== 表格：所有文字使用你指定的chinese字体 ==========
            PdfPTable table = new PdfPTable(2);
            table.setWidths(new int[]{3, 7});
            table.setWidthPercentage(80);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(PdfPCell.BOX);
            table.getDefaultCell().setPadding(8);

            // 学生姓名行
            table.addCell(new PdfPCell(new Phrase("学生姓名", chinese)));
            table.addCell(new PdfPCell(new Phrase((String) info.get("stuName"), chinese)));

            // 活动名称行
            table.addCell(new PdfPCell(new Phrase("活动名称", chinese)));
            table.addCell(new PdfPCell(new Phrase((String) info.get("actName"), chinese)));

            // 活动时间行
            table.addCell(new PdfPCell(new Phrase("活动时间", chinese)));
            table.addCell(new PdfPCell(new Phrase(
                ((java.time.LocalDateTime) info.get("startTime")).format(FMT), chinese)));

            // 活动地点行
            table.addCell(new PdfPCell(new Phrase("活动地点", chinese)));
            table.addCell(new PdfPCell(new Phrase((String) info.get("location"), chinese)));

            doc.add(table);

            // ========== 提示文字 + 二维码（使用chinese字体） ==========
            doc.add(Chunk.NEWLINE);
            doc.add(new Paragraph("请凭下方二维码签到", chinese));
            doc.add(Chunk.NEWLINE);

            byte[] qrBytes = QRUtil.generate("STU:" + stuId + ",ACT:" + actId, 200);
            Image qrImg = Image.getInstance(qrBytes);
            qrImg.setAlignment(Element.ALIGN_CENTER);
            doc.add(qrImg);

            doc.close();
            JOptionPane.showMessageDialog(null, "电子票已生成： " + file);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "生成PDF失败：" + e.getMessage());
        }
    }
}

