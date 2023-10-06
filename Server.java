import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.*;

class Server extends JFrame{

    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    //Declare Components...
    private JLabel heading=new JLabel("Server Area");
    private JTextArea messageArea=new JTextArea();
    private JTextField messageInput=new JTextField();
    private Font font=new Font("Roboto",Font.PLAIN,20);

    //Constructor...
    public Server()
    {
        try {
            server =new ServerSocket(7778);
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting... ");
            socket=server.accept();

            br=new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out=new PrintWriter(socket.getOutputStream());

            createGUI();
            headleEvents();
            startReading();
            //startWriting();

        } 
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    
    private void headleEvents() 
    {
        messageInput.addKeyListener(new KeyListener()
        {

            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
            }

            @Override
            public void keyReleased(KeyEvent e) {

            //System.out.println("key released "+e.getKeyCode());
            if(e.getKeyCode()==10)
            {
               // System.out.println("You have pressed Enter button");
               String contentToSend=messageInput.getText();
               messageArea.append("Me : "+contentToSend+"\n");
               out.println(contentToSend);
               out.flush();
               messageInput.setText("");
               messageInput.requestFocus();
            }
            }

        });
    }

    private void createGUI()
    {
        //gui code
        this.setTitle("Server Messager[END]");
        this.setSize(600,600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //coding for component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        heading.setIcon(new ImageIcon("chatlogo.png"));
        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageArea.setEditable(false);
        messageArea.setCaretPosition(messageArea.getDocument().getLength());
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);
       
        //frame layout set
        this.setLayout(new BorderLayout());

        //adding the components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane=new JScrollPane(messageArea);

        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput,BorderLayout.SOUTH);

        this.setVisible(true);        
    }


    public void startReading()
    {
        //thread read come form client...
        Runnable r1=()->{

            System.out.println("Reader started...");
            
            try{
            while(true)
            {
                String msg=br.readLine();
                if(msg.equals("Exit"))
                {
                    System.out.println("Client terminated the chat...");
                    JOptionPane.showMessageDialog(this, "Client Terminate the chat ");
                    messageInput.setEnabled(false);
                    socket.close();
                    break;
                }
                //System.out.println("Client:  "+msg);
                 messageArea.append("Client : "+msg+ "\n");
            }
            }
            catch (Exception e) {
              //  e.printStackTrace();
              System.out.println("Connection is closed.");
            }
           
        };
        new Thread(r1).start();
    }

    public void startWriting()
    {
        //thread recived from user and send to client...
        Runnable r2=()->{
            System.out.println("Writer Started.....");
            
            try{
                while(!socket.isClosed())
                {
                    BufferedReader br1=new BufferedReader(new InputStreamReader(System.in));
                    String content=br1.readLine();
                    out.println(content);
                    out.flush();

                    if(content.equals("Exit"))
                    {
                        socket.close();
                        break;
                    }
                }
            } 
            catch (Exception e) {
            //    e.printStackTrace();
            System.out.println("Connection is closed.");
            }
        };
        new Thread(r2).start();
        }


    public static void main(String[] args) {
        
        System.out.println("This is the Server going to start...");

        new Server();
    }
}