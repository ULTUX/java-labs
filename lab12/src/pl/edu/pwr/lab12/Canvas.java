package pl.edu.pwr.lab12;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class Canvas extends JPanel implements MouseMotionListener, MouseListener {
    private final Dimension resolution;
    private int[][] data;

    private boolean mouseMode;

    public Canvas(Dimension resolution) {
        super();
        System.out.println(resolution);
        this.resolution = resolution;
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        var graphics = (Graphics2D) g;
        if (data == null) return;
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                var rectPos = translatePos(new Dimension(i, j));
                if (data[i][j] == 0) {
                    graphics.setColor(new Color(1, 1, 1, 1));
                    graphics.fillRect(rectPos.width, rectPos.height, resolution.width, resolution.height);
                }
                else {
                    graphics.setColor(Color.WHITE);
                    graphics.fillRect(rectPos.width, rectPos.height, resolution.width, resolution.height);
                    graphics.setColor(Color.BLACK);
                    graphics.drawRect(rectPos.width, rectPos.height, resolution.width, resolution.height);
                }
            }
        }

    }

    public void setData(int[][] data) {
        var dataPoints = getDataSize();
        if (data != null) {
            if (data.length == dataPoints.width && data[0].length == dataPoints.height) {
                this.data = data;
                repaint();
            } else throw new IllegalArgumentException("Provided data array has invalid size.");
        }
    }

    private Dimension translatePos(Dimension d) {
        return new Dimension(resolution.width * d.width, resolution.height * d.height);
    }

    private Dimension translateBackPos(Dimension d) {
        return new Dimension(d.width/resolution.width, d.height/resolution.height);
    }

    public Dimension getDataSize() {
        var canvasSize = getSize();
        return new Dimension(canvasSize.width / resolution.width, canvasSize.height / resolution.height);
    }


    public int[][] getData() {
        return data.clone();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        var pos = new Dimension(e.getX(), e.getY());
        var index = translateBackPos(pos);
        if (index.width < 0 || index.width >= data.length || index.height < 0 || index.height >= data[0].length) return;
        data[index.width][index.height] = mouseMode ? 0 : 1;
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        var pos = new Dimension(e.getX(), e.getY());
        var index = translateBackPos(pos);
        if (index.width < 0 || index.width >= data.length || index.height < 0 || index.height >= data[0].length) return;
        data[index.width][index.height] = data[index.width][index.height] == 1 ? 0 : 1;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        var index = translateBackPos(new Dimension(e.getX(), e.getY()));
        if (index.width < 0 || index.width >= data.length || index.height < 0 || index.height >= data[0].length) return;
        mouseMode = data[index.width][index.height] != 0;
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
