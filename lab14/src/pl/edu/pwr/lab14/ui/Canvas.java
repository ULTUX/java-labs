package pl.edu.pwr.lab14.ui;

import javax.swing.*;
import java.awt.*;

public class Canvas extends JPanel {
    private boolean[] lightStates;

    private final JPanel container;
    private static final int LIGHT_RADIUS = 20;
    private static final int LIGHT_SPACING = 10;

    private static final int MARGIN = 10;

    public Canvas(Dimension size, int lightCount, JPanel container) {
        super(true);
        this.lightStates = new boolean[lightCount];
        this.container = container;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setFont(new Font("Hack", Font.PLAIN, 15));
        var graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int currPos = (getWidth() - lightStates.length * (2 * LIGHT_RADIUS + LIGHT_SPACING)) / 2 + LIGHT_RADIUS;
        for (int i = 0; i < lightStates.length; i++) {
            paintLight(graphics, new Dimension(currPos, getHeight() / 2), i);
            currPos += 2 * LIGHT_RADIUS + LIGHT_SPACING;
        }

    }

    public Dimension setLightCount(int count) {
        this.lightStates = new boolean[count];
        var newSize = new Dimension(lightStates.length * (2 * LIGHT_RADIUS + LIGHT_SPACING) + 2 * MARGIN, getHeight());
        setPreferredSize(newSize);
        container.setPreferredSize(newSize);
        repaint();
        return newSize;
    }

    private void paintLight(Graphics2D g, Dimension center, int id) {
        if (!lightStates[id]) g.setColor(Color.GRAY);
        else g.setColor(new Color(255, 205, 83));
        g.fillOval(center.width - LIGHT_RADIUS, center.height - LIGHT_RADIUS, 2 * LIGHT_RADIUS, 2 * LIGHT_RADIUS);
        g.drawString(
                String.valueOf(id),
                center.width - g.getFontMetrics().stringWidth(String.valueOf(id)) / 2,
                center.height + LIGHT_RADIUS + g.getFontMetrics().getHeight() / 2 + LIGHT_SPACING);
    }

    public void toggleLight(int lightId) {
        if (lightId < 0 || lightId >= lightStates.length) return;
        lightStates[lightId] = !lightStates[lightId];
        repaint();
    }

    public int getCurrentLightCount() {
        return lightStates.length;
    }
}
