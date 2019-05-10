import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class TP_01_DigitsUI {

    final int IMAGE_SIZE = 20 ;
    final int IMAGE_SCALED = 80 ;

    private final JLabel digit, realValue;
    private final JButton nextButton;
    private final Instances instances;
    private Random random = new Random(33) ;

    TP_01_DigitsUI() throws Exception {

        instances = TP_00_DigitsLoader.loadDigitsData() ;

        JFrame frm = new JFrame() ;
        frm.getContentPane().setLayout(new BorderLayout());

        digit = new JLabel() ;
        realValue = new JLabel("?") ;

        showDigit(instances.firstInstance());
        JPanel panel = new JPanel() ;
        panel.add(digit) ;
        frm.add(panel, BorderLayout.WEST) ;

        JPanel infosPanel = new JPanel() ;
        infosPanel.setLayout(new GridLayout(0,2));
        infosPanel.add(new JLabel("Real value : ")) ;
        infosPanel.add(realValue) ;
        frm.add(infosPanel, BorderLayout.CENTER) ;

        nextButton = new JButton("Next") ;
        frm.add(nextButton, BorderLayout.SOUTH) ;

        addListeners();

        frm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frm.pack();
        frm.setVisible(true);
    }

    private void addListeners(){
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDigit(instances.get(random.nextInt(instances.size())));
            }
        });
    }

    private void showDigit(Instance instance){
        BufferedImage img = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g = (Graphics2D)img.getGraphics() ;
        ArrayList<Attribute> attributes = Collections.list(instance.enumerateAttributes());
        int y = 0, x = 0 ; float intensity ;
        for (Attribute attr: attributes){
            intensity = (float)instance.value(attr) ;
            intensity = intensity < 0 ? 0 : intensity ;
            intensity = intensity > 1 ? 1 : intensity ;
            g.setColor(new Color(intensity, intensity, intensity));
            g.drawLine(x, y, x, y);
            x = attr.index() / 20 ;
            y = (y+1) % 20 ;
        }
        Image image = img.getScaledInstance(IMAGE_SCALED, IMAGE_SCALED, Image.SCALE_SMOOTH);;
        digit.setIcon(new ImageIcon(image));
        digit.revalidate();
        digit.repaint();
        String label = instance.classAttribute().value((int) instance.value(instance.classAttribute()));
        realValue.setText(""+Integer.valueOf(label)%10);
    }


    public static void main(String[] args) throws Exception {
        new TP_01_DigitsUI() ;
    }

}
