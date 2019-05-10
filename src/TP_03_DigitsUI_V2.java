import weka.classifiers.Classifier;
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

public class TP_03_DigitsUI_V2 {

    private final int IMAGE_SIZE = 20 ;
    private final int IMAGE_SCALED = 80 ;

    JFrame frm ;
    private JLabel digit, realValue, predicted;
    private JButton nextButton;

    private Random random = new Random(33) ;

    private Classifier classifier ;
    private Instances test;

    TP_03_DigitsUI_V2() throws Exception {

        Instances dataset = TP_00_DigitsLoader.loadDigitsData() ;

        Instances[] split = TP_02_DigitsClassifierFinder.split(dataset, 80) ;
        Instances train = split[0] ;
        test = split[1] ;

        classifier = TP_02_DigitsClassifierFinder.findBestClassifier(train) ;

        buildUI() ;

        showDigit(test.firstInstance());
        frm.pack();
        frm.setVisible(true);
    }

    private void buildUI(){
        frm = new JFrame() ;
        frm.getContentPane().setLayout(new BorderLayout());

        digit = new JLabel() ;
        realValue = new JLabel("?") ;
        predicted = new JLabel("?") ;

        JPanel titlePanel = new JPanel() ;
        JLabel label = new JLabel("Classifier : " + classifier.getClass().getSimpleName()) ;
        label.setFont(new Font("Serif", Font.BOLD, 22));
        titlePanel.add(label) ;
        frm.add(titlePanel, BorderLayout.NORTH) ;

        frm.add(digit, BorderLayout.WEST) ;

        JPanel infosPanel = new JPanel() ;
        infosPanel.setLayout(new GridLayout(0,2));
        infosPanel.add(new JLabel("Real : ")) ;
        infosPanel.add(realValue) ;
        infosPanel.add(new JLabel("Predicted : ")) ;
        infosPanel.add(predicted) ;
        frm.add(infosPanel, BorderLayout.CENTER) ;

        nextButton = new JButton("Next") ;
        frm.add(nextButton, BorderLayout.SOUTH) ;

        addListeners();

        frm.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    private void addListeners(){
        nextButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDigit(test.get(random.nextInt(test.size())));
            }
        });
    }

    private void showDigit(Instance instance) {
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
        String label ;
        label = instance.classAttribute().value((int) instance.value(instance.classAttribute()));
        realValue.setText(""+Integer.valueOf(label)%10);
        try {
            label = instance.classAttribute().value((int) classifier.classifyInstance(instance));
            predicted.setText("" + Integer.valueOf(label) % 10);
        }catch (Exception ex){ ex.printStackTrace(); }
    }


    public static void main(String[] args) throws Exception {
        new TP_03_DigitsUI_V2() ;
    }

}
