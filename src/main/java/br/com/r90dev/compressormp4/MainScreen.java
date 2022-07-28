package br.com.r90dev.compressormp4;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
 
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
 
 
public class MainScreen extends JFrame {
 
    private JButton buttonConvert;
    private String pathFile ="";
    public MainScreen() {
        super("*BETA* Otimizador de vídeos MP4");
        
        
        
        
        
        this.buttonConvert = buttonConvert;
        setLayout(new FlowLayout(FlowLayout.LEFT, 20, 15));
 
        JLabel lAviso = new JLabel("<html><h3 style='color:#f48225'>Avisos</h3>"
                + "Funciona somente com arquivos .MP4<br/>"
                + "Os arquivos convertidos serão salvos na pasta OUTPUT"
                + "</html>");
        add(lAviso);
        // set up a file picker component
        JFilePicker filePicker = new JFilePicker("Arquivo mp4", "Escolha o vídeo...");
        filePicker.setMode(JFilePicker.MODE_OPEN);
        filePicker.addFileTypeFilter(".mp4", "MPEG-4 Vídeos");
         
        // access JFileChooser class directly
        JFileChooser fileChooser = filePicker.getFileChooser();
        //fileChooser.setCurrentDirectory(new File("D:/"));
         
        // add the component to the frame
        add(filePicker);
        
        JLabel lPartes = new JLabel("Número de partes ");
        
        add(lPartes);
        
        JTextField tfPartes = new JTextField(5);
        
        tfPartes.setText("2");
        //tfPartes.set
        add(tfPartes);
        pathFile = filePicker.getSelectedFilePath();
        System.out.println("" + pathFile);
        buttonConvert = new JButton("                                      CORTAR E COMPRIMIR                                      ");
        buttonConvert.setSize(200, 100);
        buttonConvert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent evt) {
                pathFile = filePicker.getSelectedFilePath();
                //System.out.println("" + pathFile);
                int partes = 0;
                
                try{
                    partes = Integer.parseInt(tfPartes.getText());
                }catch(Exception ex){
                    JOptionPane.showMessageDialog(null,"Informe um número válido de partes","Ops", JOptionPane.WARNING_MESSAGE);
                }
                if(partes > 0 && pathFile.length()> 0){
                    try{
                        CortaFFmpeg cut = new CortaFFmpeg();
                        cut.split(pathFile,partes);
                        JOptionPane.showMessageDialog(null,"Trabalho concluído!","Finalizado", JOptionPane.INFORMATION_MESSAGE);
                        
                    
                    }catch(Exception ex){
                         JOptionPane.showMessageDialog(null,"Ocorreu um erro no processo.","Ops", JOptionPane.WARNING_MESSAGE);
                    }
                    //buttonActionPerformed(evt);          
                } else {
                    JOptionPane.showMessageDialog(null,"Preencha todos os campos corretamente","Ops", JOptionPane.WARNING_MESSAGE);
                }
            }

            
        });
        
        
        add(buttonConvert);
        
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(520, 280);
        setLocationRelativeTo(null);    // center on screen
        
        File splitFile = new File(System.getProperty("user.dir") + "/ffmpeg");//Destination folder to save.
        if (!splitFile.exists()) {
            JOptionPane.showMessageDialog(null,"Biblioteca ffmpeg não encontrada na pasta " + System.getProperty("user.dir"),"Ops", JOptionPane.WARNING_MESSAGE);
            
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainScreen().setVisible(true);
            }
        });
    }
 
}