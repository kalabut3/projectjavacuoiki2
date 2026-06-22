package ui;

import service.StatisticsService;
import javax.swing.*;
import java.awt.*;

public class ChartPanel extends JPanel {

    private final int todo, doing, done;

    public ChartPanel() {
        StatisticsService s = new StatisticsService();
        todo  = s.getTodo();
        doing = s.getDoing();
        done  = s.getDone();
        setOpaque(false);
        // Sensible default size; actual size is driven by the parent
        // LayoutManager since paintComponent already reads getWidth/getHeight.
        setPreferredSize(new Dimension(400, 260));
        setMinimumSize(new Dimension(220, 160));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,   RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int W=getWidth(), H=getHeight();
        int padL=60, padB=54, padT=24, padR=20;
        int chartW=W-padL-padR, chartH=H-padT-padB;

        // Background grid lines
        g2.setStroke(new BasicStroke(1f));
        int max=Math.max(1, Math.max(todo, Math.max(doing, done)));
        int gridLines=4;
        for(int i=0;i<=gridLines;i++){
            int gy=padT+chartH-chartH*i/gridLines;
            g2.setColor(UITheme.BORDER);
            g2.drawLine(padL, gy, padL+chartW, gy);
            g2.setColor(UITheme.TEXT_LOW);
            g2.setFont(UITheme.FONT_SMALL);
            String val=String.valueOf(max*i/gridLines);
            g2.drawString(val, padL-g2.getFontMetrics().stringWidth(val)-6, gy+5);
        }

        // Axis
        g2.setColor(UITheme.BORDER_LIGHT);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(padL, padT, padL, padT+chartH);
        g2.drawLine(padL, padT+chartH, padL+chartW, padT+chartH);

        // Bars
        int numBars=3;
        int barW=50, gap=(chartW-numBars*barW)/(numBars+1);

        drawBar(g2, padL+gap,            padT, chartH, todo,  max, UITheme.RED,    "TODO",    String.valueOf(todo));
        drawBar(g2, padL+gap*2+barW,     padT, chartH, doing, max, UITheme.ORANGE, "DOING",   String.valueOf(doing));
        drawBar(g2, padL+gap*3+barW*2,   padT, chartH, done,  max, UITheme.GREEN,  "DONE",    String.valueOf(done));
    }

    private void drawBar(Graphics2D g2, int x, int padT, int chartH, int val, int max,
                         Color color, String label, String valStr){
        int barW=50, bh=max==0?0:val*chartH/max;
        int by=padT+chartH-bh;

        // shadow
        g2.setColor(new Color(0,0,0,60));
        g2.fillRoundRect(x+4, by+4, barW, bh, 8, 8);

        // bar gradient
        GradientPaint gp=new GradientPaint(x, by, color.brighter(), x, by+bh, color.darker());
        g2.setPaint(gp);
        g2.fillRoundRect(x, by, barW, bh, 8, 8);

        // value above bar
        g2.setColor(color);
        g2.setFont(new Font("Arial",Font.BOLD,13));
        FontMetrics fm=g2.getFontMetrics();
        g2.drawString(valStr, x+(barW-fm.stringWidth(valStr))/2, by-6);

        // label below axis
        g2.setColor(UITheme.TEXT_MID);
        g2.setFont(UITheme.FONT_SMALL);
        fm=g2.getFontMetrics();
        g2.drawString(label, x+(barW-fm.stringWidth(label))/2, padT+chartH+20);
    }
}
