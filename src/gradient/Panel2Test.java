package gradient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Panel2Test
{
    public static void main(String[] args)
    {
        ShadeOptionsPanel shadeOptions = new ShadeOptionsPanel();
        ShadowSelector shadowSelector = new ShadowSelector(shadeOptions);
        ComponentSource componentSource = new ComponentSource(shadeOptions);
        JFrame f = new JFrame("Concept Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(shadowSelector, "North");
        f.getContentPane().add(shadeOptions);
        f.getContentPane().add(componentSource, "South");
        f.setSize(400,400);
        f.setLocation(200,200);
        f.setVisible(true);
    }
}

class ShadeOptionsPanel extends JPanel
{
    final int PAD, DIA, BORDER;
    Color colorIn, colorOut;
    int xc, yc;
    Ellipse2D eIn, eOut;
    GradientPaint gradient;
    CustomPaint customPaint;
    Area arcBorder;
    int width, height;
    Point2D
        neOrigin, nwOrigin, swOrigin, seOrigin,
        neDiag, nwDiag, swDiag, seDiag;
    int shadowVertex = 3;
    final static int
        NORTHEAST = 0,
        NORTHWEST = 1,
        SOUTHWEST = 2,
        SOUTHEAST = 3;

    public ShadeOptionsPanel()
    {
        PAD = 25;
        DIA = 75;
        BORDER = 10;
        colorIn = Color.black;
        colorOut = getBackground();
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
        width = getWidth();
        height = getHeight();
        g2.drawRoundRect(PAD, PAD, width - 2*PAD, height - 2*PAD, DIA, DIA);

        calculateArcOrigins();

        calculateCardinalDiagonals();

        drawVertexArc(g2, shadowVertex);

        switch(shadowVertex)
        {
            case NORTHEAST:
                drawNorthSide(g2);
                drawEastSide(g2);

                // draw northwest arc
                xc = PAD + DIA/2;
                yc = PAD + DIA/2;
                customPaint = new CustomPaint(xc, yc, new Point2D.Double(0, DIA/2),
                                              DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = getInnerEllipse(nwOrigin, nwDiag);
                eOut = getOuterEllipse(nwOrigin, nwDiag);
                arcBorder = getArcArea(eIn, eOut, 90.0);
                g2.fill(arcBorder);

                // draw southeast arc
                xc = width  - PAD - DIA/2;
                yc = height - PAD - DIA/2;
                customPaint = new CustomPaint(xc, yc, new Point2D.Double(0, DIA/2),
                                              DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = getInnerEllipse(seOrigin, seDiag);
                eOut = getOuterEllipse(seOrigin, seDiag);
                arcBorder = getArcArea(eIn, eOut, 270.0);
                g2.fill(arcBorder);
                break;
            case NORTHWEST:
                drawNorthSide(g2);
                drawWestSide(g2);

                // draw northeast arc
                xc = width - PAD - DIA/2;
                yc = PAD + DIA/2;
                customPaint = new CustomPaint(xc, yc, new Point2D.Double(0, DIA/2),
                                              DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = getInnerEllipse(neOrigin, neDiag);
                eOut = getOuterEllipse(neOrigin, neDiag);
                arcBorder = getArcArea(eIn, eOut, 0.0);
                g2.fill(arcBorder);

                // draw southwest arc
                xc = PAD + DIA/2;
                yc = height - PAD - DIA/2;
                customPaint = new CustomPaint(xc, yc, new Point2D.Double(0, DIA/2),
                                              DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = getInnerEllipse(swOrigin, swDiag);
                eOut = getOuterEllipse(swOrigin, swDiag);
                arcBorder = getArcArea(eIn, eOut, 180.0);
                g2.fill(arcBorder);
                break;
            case SOUTHWEST:
                drawWestSide(g2);
                drawSouthSide(g2);

                // draw northwest arc
                xc = PAD + DIA/2;
                yc = PAD + DIA/2;
                customPaint = new CustomPaint(xc, yc, new Point2D.Double(0, DIA/2),
                                              DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = getInnerEllipse(nwOrigin, nwDiag);
                eOut = getOuterEllipse(nwOrigin, nwDiag);
                arcBorder = getArcArea(eIn, eOut, 90.0);
                g2.fill(arcBorder);

                // draw the southeast arc
                xc = width  - PAD - DIA/2;
                yc = height - PAD - DIA/2;
                customPaint = new CustomPaint(xc, yc, new Point2D.Double(0, DIA/2),
                                              DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = getInnerEllipse(seOrigin, seDiag);
                eOut = getOuterEllipse(seOrigin, seDiag);
                arcBorder = getArcArea(eIn, eOut, 270.0);
                g2.fill(arcBorder);
                break;
            case SOUTHEAST:
                drawEastSide(g2);
                drawSouthSide(g2);

                // draw northeast arc
                xc = width - PAD - DIA/2;
                yc = PAD + DIA/2;
                customPaint = new CustomPaint(xc, yc, new Point2D.Double(0, DIA/2),
                                              DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = getInnerEllipse(neOrigin, neDiag);
                eOut = getOuterEllipse(neOrigin, neDiag);
                arcBorder = getArcArea(eIn, eOut, 0.0);
                g2.fill(arcBorder);

                // draw southwest arc
                xc = PAD + DIA/2;
                yc = height - PAD - DIA/2;
                customPaint = new CustomPaint(xc, yc, new Point2D.Double(0, DIA/2),
                                              DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = getInnerEllipse(swOrigin, swDiag);
                eOut = getOuterEllipse(swOrigin, swDiag);
                arcBorder = getArcArea(eIn, eOut, 180.0);
                g2.fill(arcBorder);
        }
    }

    private Ellipse2D getInnerEllipse(Point2D center, Point2D corner)
    {
        return new Ellipse2D.Double(center.getX() - DIA/2,
                                    center.getY() - DIA/2, DIA, DIA);
    }

    private Ellipse2D getOuterEllipse(Point2D center, Point2D corner)
    {
        int
            w = DIA,
            h = DIA;

        if(shadowVertex < 2)
            if(center.getY() > corner.getY())
                h += 2 * BORDER;
            else
                w += 2 * BORDER;
        else
            if(center.getY() > corner.getY())
                w += 2 * BORDER;
            else
                h += 2 * BORDER;

        return new Ellipse2D.Double(center.getX() - w/2, center.getY() - h/2, w, h);
    }

    private Area getArcArea(Ellipse2D e1, Ellipse2D e2, double start)
    {
        Arc2D arc1 = new Arc2D.Double(e1.getBounds2D(), start, 90.0, Arc2D.PIE);
        Arc2D arc2 = new Arc2D.Double(e2.getBounds2D(), start, 90.0, Arc2D.PIE);
        Area arc = new Area(arc2);
        arc.subtract(new Area(arc1));
        return arc;
    }

    private void drawNorthSide(Graphics2D g2)
    {
        gradient = new GradientPaint(width/2, PAD - BORDER, colorOut,
                                     width/2, PAD, colorIn);
        g2.setPaint(gradient);
        g2.fill(new Rectangle2D.Double(PAD + DIA/2, PAD - BORDER,
                                       width - 2*(PAD + DIA/2) + 1, BORDER));
    }

    private void drawWestSide(Graphics2D g2)
    {
        gradient = new GradientPaint(PAD - BORDER, height/2, colorOut,
                                     PAD, height/2, colorIn);
        g2.setPaint(gradient);
        g2.fill(new Rectangle2D.Double(PAD - BORDER, PAD + DIA/2,
                                       BORDER, height - 2*(PAD + DIA/2) + 1));
    }

    private void drawSouthSide(Graphics2D g2)
    {
        gradient = new GradientPaint(width/2, height - PAD, colorIn,
                                     width/2, height - PAD + BORDER, colorOut);
        g2.setPaint(gradient);
        g2.fill(new Rectangle2D.Double(PAD + DIA/2, height - PAD,
                                       width - 2*(PAD + DIA/2) + 1, BORDER));
    }

    private void drawEastSide(Graphics2D g2)
    {
        gradient = new GradientPaint(width - PAD, height/2, colorIn,
                                     width - PAD + BORDER, height/2, colorOut);
        g2.setPaint(gradient);
        g2.fill(new Rectangle2D.Double(width - PAD, PAD + DIA/2,
                                       BORDER, height - 2*(PAD + DIA/2) + 1));
    }

    /**
     * Draws the central, full-shaded arc (opposite of the unshaded arc).
     */
    private void drawVertexArc(Graphics2D g2, int index)
    {
        switch(index)
        {
            case NORTHEAST:
                xc = width - PAD - DIA/2;
                yc = PAD + DIA/2;
                customPaint = new CustomPaint(xc, yc, new Point2D.Double(0, DIA/2),
                                              DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = new Ellipse2D.Double(width - PAD - DIA, PAD, DIA, DIA);
                eOut = new Ellipse2D.Double(width - PAD - DIA - BORDER, PAD - BORDER,
                                            DIA + 2*BORDER, DIA + 2*BORDER);
                arcBorder = getArcArea(eIn, eOut, 0.0);
                g2.fill(arcBorder);
                break;
            case NORTHWEST:
                xc = PAD + DIA/2;
                yc = PAD + DIA/2;
                customPaint = new CustomPaint(xc, yc, new Point2D.Double(0, DIA/2),
                                              DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = new Ellipse2D.Double(PAD, PAD, DIA, DIA);
                eOut = new Ellipse2D.Double(PAD - BORDER, PAD - BORDER,
                                            DIA + 2*BORDER, DIA + 2*BORDER);
                arcBorder = getArcArea(eIn, eOut, 90.0);
                g2.fill(arcBorder);
                break;
            case SOUTHWEST:
                xc = PAD + DIA/2;
                yc = height - PAD - DIA/2;
                customPaint = new CustomPaint(xc, yc, new Point2D.Double(0, DIA/2),
                                              DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = new Ellipse2D.Double(PAD, height - PAD - DIA, DIA, DIA);
                eOut = new Ellipse2D.Double(PAD - BORDER, height - PAD - DIA - BORDER,
                                            DIA + 2*BORDER, DIA + 2*BORDER);
                arcBorder = getArcArea(eIn, eOut, 180.0);
                g2.fill(arcBorder);
                break;
            case SOUTHEAST:
                xc = width  - PAD - DIA/2;
                yc = height - PAD - DIA/2;
                customPaint = new CustomPaint(xc, yc, new Point2D.Double(0, DIA/2),
                                              DIA/2, BORDER, colorIn, colorOut);
                g2.setPaint(customPaint);
                eIn  = new Ellipse2D.Double(width - PAD - DIA,
                                            height - PAD - DIA, DIA, DIA);
                eOut = new Ellipse2D.Double(width - PAD - DIA - BORDER,
                                            height - PAD - DIA - BORDER,
                                            DIA + 2*BORDER, DIA + 2*BORDER);
                arcBorder = getArcArea(eIn, eOut, 270.0);
                g2.fill(arcBorder);
        }
    }

    private void calculateArcOrigins()
    {
        neOrigin = new Point2D.Double(width - PAD - DIA/2, PAD + DIA/2);
        nwOrigin = new Point2D.Double(PAD + DIA/2, PAD + DIA/2);
        swOrigin = new Point2D.Double(PAD + DIA/2, height - PAD - DIA/2);
        seOrigin = new Point2D.Double(width - PAD - DIA/2, height - PAD - DIA/2);
    }

    private void calculateCardinalDiagonals()
    {
        neDiag = new Point2D.Double(neOrigin.getX() +
                                        DIA * Math.cos(Math.toRadians(45))/2,
                                    neOrigin.getY() -
                                        DIA * Math.sin(Math.toRadians(45))/2);
        nwDiag = new Point2D.Double(nwOrigin.getX() +
                                        DIA * Math.cos(Math.toRadians(135))/2,
                                    nwOrigin.getY() -
                                        DIA * Math.sin(Math.toRadians(135))/2);
        swDiag = new Point2D.Double(swOrigin.getX() +
                                        DIA * Math.cos(Math.toRadians(225))/2,
                                    swOrigin.getY() -
                                        DIA * Math.sin(Math.toRadians(225))/2);
        seDiag = new Point2D.Double(seOrigin.getX() +
                                        DIA * Math.cos(Math.toRadians(315))/2,
                                    seOrigin.getY() -
                                        DIA * Math.sin(Math.toRadians(315))/2);
    }

    public Dimension getInnerSize()
    {
        return new Dimension((int)nwOrigin.distance(neOrigin),
                             (int)nwOrigin.distance(swOrigin));
    }
}

/**
 * JSpinner to select the type of shadow for the ShadeOptionsPanel.
 * The direction is the fully shaded corner, adjacent corners being
 * partially shaded and the opposite corner unshaded.
 * Loaded into the north section of the content pane in main method.
 */
class ShadowSelector extends JPanel
{
    ShadeOptionsPanel soPanel;

    public ShadowSelector(ShadeOptionsPanel sop)
    {
        soPanel = sop;
        String[] directions = {
            "northeast", "northwest", "southwest", "southeast"
        };
        final SpinnerListModel model = new SpinnerListModel(directions);
        model.setValue(directions[3]);
        JSpinner spinner = new JSpinner(model);
        spinner.setPreferredSize(new Dimension(90, spinner.getPreferredSize().height));
        spinner.addChangeListener(new ChangeListener()
        {
            public void stateChanged(ChangeEvent e)
            {
                String value = (String)model.getValue();
                soPanel.shadowVertex = model.getList().indexOf(value);
                soPanel.repaint();
            }
        });
        add(new JLabel("shadow vertex", JLabel.RIGHT));
        add(spinner);
    }
}

/**
 * A button to add a component panel to the ShadedOptionsPanel
 * and a button to add buttons to this component panel.
 * Loaded into the south section of content pane in main method.
 */
class ComponentSource extends JPanel
{
    ShadeOptionsPanel soPanel;
    JPanel panel;

    public ComponentSource(ShadeOptionsPanel sop)
    {
        soPanel = sop;
        panel = new JPanel();
        final JButton
            addPanelButton = new JButton("add panel"),
            addButton = new JButton("add button");
        ActionListener l = new ActionListener()
        {
            int buttonCount = 0;

            public void actionPerformed(ActionEvent e)
            {
                JButton button = (JButton)e.getSource();
                if(button == addButton)
                {
                    if(addPanelButton.isEnabled())
                        return;
                    panel.add(new JButton("Button " + ++buttonCount));
                    panel.validate();
                    panel.repaint();
                }
                if(button == addPanelButton)
                {
                    panel.setBorder(BorderFactory.createEtchedBorder());
                    panel.setPreferredSize(soPanel.getInnerSize());
                    soPanel.setLayout(new GridBagLayout());
                    soPanel.add(panel, new GridBagConstraints());
                    soPanel.validate();
                    soPanel.repaint();
                    addPanelButton.setEnabled(false);
                }
            }
        };
        addPanelButton.addActionListener(l);
        addButton.addActionListener(l);
        add(addPanelButton);
        add(addButton);
    }
}
