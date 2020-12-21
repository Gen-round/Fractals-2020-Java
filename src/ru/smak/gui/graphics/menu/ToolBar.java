package ru.smak.gui.graphics.menu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.TimerTask;

public class ToolBar extends JToolBar {
    private JToolBar toolBar;
    private JButton back;
    private JCheckBox dynamicDet;
    private JButton animation;
    private JComboBox fractal;
    private JComboBox color;
    private ArrayList<ColorChooseListener> cc = new ArrayList<>();
    private ArrayList<MandelbrotChooseListener> mc = new ArrayList<>();


    public ToolBar(JToolBar bar){
        toolBar = bar;
        back = new JButton(MainMenu.createIcon("icons/back.png"));
        toolBar.add(back);
        toolBar.addSeparator();
        dynamicDet = new JCheckBox("Детализация", false);
        dynamicDet.setBorderPaintedFlat(true);
        toolBar.add(dynamicDet);
        toolBar.addSeparator();
        animation = new JButton(MainMenu.createIcon("icons/video.png"));
        fractal = new JComboBox(new String[]{"z^2+c","z^3+c", "z^4+c", "z^9+c"});
        toolBar.add(fractal);
        toolBar.addSeparator();
        color = new JComboBox(new String[]{"Разноцветный", "Синий", "Зелёный", "Жёлтый", "Коричневый"});
        toolBar.add(color);
        toolBar.addSeparator();
        toolBar.add(animation);
        toolBar.setFloatable(false);

        back.addMouseListener(new MouseAdapter() {
            private int clickCnt = 0;
            java.util.Timer timer = new java.util.Timer("doubleClickTimer", false);
            @Override
            public void mouseClicked(final MouseEvent e) {
                clickCnt = e.getClickCount();
                if (e.getClickCount() == 1) {
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            if (clickCnt == 1) {
                                //вернуть на шаг раньше
                            } else if (clickCnt > 1) {
                                //вернуться к исходному фракталу
                            }
                            clickCnt = 0;
                        }
                    }, 300);
                }
            }
        });

        dynamicDet.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //использовать динамическую детализацию
            }
        });

        color.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeColor(color.getSelectedIndex());
            }
        });
        fractal.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeView(fractal.getSelectedIndex());
            }
        });
        animation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //создать видео
            }
        });
    }
    public void addCChooseListener(ColorChooseListener c){
        cc.add(c);
    }
    public void changeColor(int i){
        for(var c: cc){
            c.chooseColor(i);
        }
    }
    public void addMChooseListener(MandelbrotChooseListener m){
        mc.add(m);
    }
    public void changeView(int i){
        for(var m : mc )
            m.chooseFractal(i);
    }

}