package ru.smak.gui;

import ru.smak.gui.graphics.FinishedListener;
import ru.smak.gui.graphics.FractalPainter;
import ru.smak.gui.graphics.SelectionPainter;
import ru.smak.gui.graphics.components.GraphicsPanel;
import ru.smak.gui.graphics.coordinates.CartesianScreenPlane;
import ru.smak.gui.graphics.coordinates.Converter;

import ru.smak.gui.graphics.fractalcolors.*;
import ru.smak.gui.graphics.menu.ColorChooseListener;
import ru.smak.gui.graphics.menu.MainMenu;
import ru.smak.gui.graphics.menu.MandelbrotChooseListener;
import ru.smak.gui.graphics.menu.ToolBar;

import ru.smak.math.Mandelbrot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame {
    GraphicsPanel mainPanel;

    static final Dimension MIN_SIZE = new Dimension(450, 350);
    static final Dimension MIN_FRAME_SIZE = new Dimension(600, 500);

    ColorScheme1 c1 = new ColorScheme1();
    ColorScheme2 c2 = new ColorScheme2();
    ColorScheme3 c3 = new ColorScheme3();
    ColorScheme4 c4 = new ColorScheme4();
    ColorScheme5 c5 = new ColorScheme5();
    Colorizer[] colorScheme = new Colorizer[]{c1,c2, c3, c4, c5};;
    Colorizer colorizer = c1;
    CartesianScreenPlane plane;
    Mandelbrot mandelbrot = new Mandelbrot();
    FractalPainter fp;
  
    public MainWindow(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(MIN_FRAME_SIZE);
        setTitle("Фракталы");

        mainPanel = new GraphicsPanel();

        mainPanel.setBackground(Color.WHITE);
        JMenuBar menuBar = new JMenuBar();
        MainMenu menu = new MainMenu(menuBar);
        setJMenuBar(menuBar);

        JToolBar toolBar = new JToolBar();
        ToolBar tb = new ToolBar(toolBar);

        GroupLayout gl = new GroupLayout(getContentPane());
        setLayout(gl);
        gl.setVerticalGroup(gl.createSequentialGroup()
                .addGap(4)
                .addComponent(toolBar,(int)(MIN_SIZE.height*0.1), GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                .addGap(4)
                .addComponent(mainPanel, (int)(MIN_SIZE.height*0.8), MIN_SIZE.height, GroupLayout.DEFAULT_SIZE)
                .addGap(4)
        );
        gl.setHorizontalGroup(gl.createSequentialGroup()
                .addGap(4)
                .addGroup(gl.createParallelGroup()
                        .addComponent(toolBar, (int)(MIN_SIZE.height*0.1),GroupLayout.PREFERRED_SIZE,GroupLayout.PREFERRED_SIZE)
                        .addComponent(mainPanel, MIN_SIZE.width, MIN_SIZE.width, GroupLayout.DEFAULT_SIZE)
                )
                .addGap(4)
        );
        pack();
        var plane = new CartesianScreenPlane(
                mainPanel.getWidth(),
                mainPanel.getHeight(),
                -2, 1, -1, 1
        );
        mandelbrot.setPlane(plane);


        fp = new FractalPainter(plane, mandelbrot);
        fp.col = colorizer;
        mandelbrot.setStockParams(plane.xMin,plane.xMax,plane.yMin,plane.yMax);
        fp.addFinishedListener(new FinishedListener() {
            @Override
            public void finished() {
                mainPanel.repaint();
            }
        });
        mainPanel.addPainter(fp);
        var sp = new SelectionPainter(mainPanel.getGraphics());

        mainPanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                plane.setWidth(mainPanel.getWidth());
                plane.setHeight(mainPanel.getHeight());
                sp.setGraphics(mainPanel.getGraphics());
            }
        });
        mainPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                sp.setVisible(true);
                sp.setStartPoint(e.getPoint());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                sp.setVisible(false);
                var r = sp.getSelectionRect();
                var xMin = Converter.xScr2Crt(r.x, plane);
                var xMax = Converter.xScr2Crt(r.x+r.width, plane);
                var yMin  = Converter.yScr2Crt(r.y+r.height, plane);
                var yMax = Converter.yScr2Crt(r.y, plane);
                plane.xMin = xMin;
                plane.xMax = xMax;
                plane.yMin = yMin;
                plane.yMax = yMax;
                mainPanel.repaint();
            }
        });

        mainPanel.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseDragged(e);
                sp.setCurrentPoint(e.getPoint());
            }
        });
        tb.addCChooseListener(new ColorChooseListener() {
            @Override
            public void chooseColor(int i) {
                colorizer = colorScheme[i];
                fp.col = colorizer;
                mainPanel.repaint();
            }
        });

        tb.addMChooseListener(new MandelbrotChooseListener() {
            @Override
            public void chooseFractal(int i) {
                mandelbrot = fractals[i];
                fp = new FractalPainter(plane,mandelbrot);
                fp.col = colorizer;
                mainPanel.addPainter(fp);
                mainPanel.repaint();
            }
        });
    }
}
