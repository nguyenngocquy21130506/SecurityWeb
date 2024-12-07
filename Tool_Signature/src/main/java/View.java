import Algorithm.DigitalSignature;
import Algorithm.RSA;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

public class View extends JFrame {

    JTextArea message, result, privateKey;
    JButton loadPrivateKeyButton, saveSignature;
    JScrollPane scrollPaneMessage;
    StringBuilder fileContent;

    public View() {
        setTitle("Chữ ký điện tử");
        setSize(800, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        add(progressBar);

        privateKey = new JTextArea();
        privateKey.setEditable(false);
        privateKey.setAlignmentY(Component.CENTER_ALIGNMENT);
        JScrollPane privateKeyScrollPane = new JScrollPane(privateKey);
        privateKeyScrollPane.setBounds(20, 10, 650, 60);
        privateKeyScrollPane.setBorder(BorderFactory.createTitledBorder("File chứa khóa riêng tư"));
        add(privateKeyScrollPane);

        loadPrivateKeyButton = new JButton("Nhập");
        loadPrivateKeyButton.setBounds(670, 19, 100, 49);
        add(loadPrivateKeyButton);

        JLabel arrow = new JLabel();
        ImageIcon iconArrow = new ImageIcon(View.class.getResource("/imgs/next.png"));
        Image imageArrow = iconArrow.getImage();
        Image scaledArrow = imageArrow.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        Icon scaledIcoArrow = new ImageIcon(scaledArrow);
        arrow.setIcon(scaledIcoArrow);
        arrow.setBounds(380, 170, 30, 30);
        add(arrow);

        // Nội dung đầu vào
        message = new JTextArea();
        message.setLineWrap(true);
        message.setWrapStyleWord(true);
        message.setEditable(false);
        scrollPaneMessage = new JScrollPane(message);
        scrollPaneMessage.setBorder(BorderFactory.createTitledBorder("File dữ liệu đơn hàng"));
        scrollPaneMessage.setBounds(20, 80, 360, 200);
        add(scrollPaneMessage);

        // Nút Load File (file đơn hàng duoc lưu trên máy)
        JButton loadFileButton = new JButton("Tải file lên");
        loadFileButton.setBounds(20, 280, 360, 30);
        add(loadFileButton);

        // Kết quả
        result = new JTextArea();
        result.setLineWrap(true);
        result.setWrapStyleWord(true);
        result.setEditable(false);
        JScrollPane scrollPaneResult = new JScrollPane(result);
        scrollPaneResult.setBorder(BorderFactory.createTitledBorder("Chữ ký"));
        scrollPaneResult.setBounds(410, 80, 360, 200);
        add(scrollPaneResult);

        // Nút Lưu chữ ký
        saveSignature = new JButton("Lưu Chữ Ký");
        saveSignature.setBounds(410, 280, 360, 30);
        add(saveSignature);

        // Nút ký
        JButton buttonSign = new JButton("Ký");
        buttonSign.setBounds(250, 320, 300, 30);
        buttonSign.setToolTipText("Ký dữ liệu đơn hàng");
        add(buttonSign);

        // Nút Hướng dẫn
        JLabel guide = new JLabel();
        guide.setToolTipText("Hướng dẫn sử dụng");
        ImageIcon iconQuestion = new ImageIcon(View.class.getResource("/imgs/question.png"));
        Image imageQuestion = iconQuestion.getImage();
        Image scaledQuestion = imageQuestion.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        Icon scaledIcoQuestion = new ImageIcon(scaledQuestion);
        guide.setIcon(scaledIcoQuestion);
        guide.setBounds(740, 320, 30, 30);
        add(guide);

        setVisible(true);

        guide.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Hiển thị hộp thoại hướng dẫn
                showGuideDialog();
            }
        });

        // Su kien load file de ma hoa
        loadFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mở JFileChooser để chọn file
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();
                    message.setText(filePath);

                    List<String> lines = null;
                    try {
                        lines = Files.readAllLines(Paths.get(filePath));
                        fileContent = new StringBuilder();
                        for (int i = 1; i < lines.size(); i++) {
                            fileContent.append(lines.get(i)).append("\n");
                        }
                        message.setToolTipText("<html>" + fileContent.toString().trim().replace("\n", "<br>") + "</html>");
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });

        // Them xu ly cho nut luu chu ky
        saveSignature.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String signValue = result.getText();
                if (signValue == null || signValue.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Chữ ký đang trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();

                    try {
                        FileWriter writer = new FileWriter(filePath);
                        writer.write(signValue);
                        writer.close();
                        JOptionPane.showMessageDialog(null, "Chữ ký đã được lưu vào "+filePath, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi lưu file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Them xu ly cho nut Load Private key
        loadPrivateKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();

                    try {
                        List<String> lines = Files.readAllLines(Paths.get(filePath));
                        if (lines.size() > 0 && lines.get(0).equals("-----BEGIN PRIVATE KEY-----")) {
                            privateKey.setText(filePath);
                        } else {
                            JOptionPane.showMessageDialog(null, "File không hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi đọc file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        // Them xu ly cho nut Encrypt
        buttonSign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result.setText("");

                if (message.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Vui lòng tải file dữ liệu cần mã hóa lên", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Hiển thị progress bar
                progressBar.setVisible(true);
                progressBar.setBounds(0, 550, 560, 50);
                progressBar.setIndeterminate(false); // Chuyển sang chế độ xác định
                progressBar.setMaximum(20); // Giả sử có 20 bước

                // Sử dụng SwingWorker để xử lý mã hóa
                SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        // Cập nhật tiến trình
                        for (int i = 0; i <= 20; i++) {
                            Thread.sleep(10); // Giả lập thời gian xử lý
                            publish(i); // Gửi tiến trình cập nhật
                        }
                        signatureFile();
                        return null;
                    }

                    @Override
                    protected void process(java.util.List<Integer> chunks) {
                        for (Integer progress : chunks) {
                            progressBar.setIndeterminate(false); // Đặt về chế độ xác định
                            progressBar.setValue(progress); // Cập nhật giá trị tiến trình
                        }
                    }

                    @Override
                    protected void done() {
                        progressBar.setVisible(false);
                    }
                };
                worker.execute(); // Bắt đầu SwingWorker
            }
        });
    }

    // Ham ky chu ky dien tu dang file
    private void signatureFile() {
        try {
            DigitalSignature signature = new DigitalSignature();
            if (privateKey.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đúng định dạng khóa", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            List<String> lines = Files.readAllLines(Paths.get(privateKey.getText()));
            StringBuilder privateKeyContent = new StringBuilder();
            for (int i = 1; i < lines.size(); i++) {
                privateKeyContent.append(lines.get(i)).append("\n");
            }
            signature.setPrivateKey(Base64.getDecoder().decode(privateKeyContent.toString().trim()));
            String hashMessage = "";
            hashMessage = signature.signFile(new File(message.getText()), signature.getPrivateKey(), "SHA512withRSA");
            if (hashMessage.isEmpty()) {
                return;
            }
            result.setText(hashMessage);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showGuideDialog() {
        String guideText = "<html>" +
                "<h2 style='text-align:center;'>Hướng dẫn sử dụng</h2> </br>" +
                "<p style='padding-top:3px'>Bước 1: Nhấn nút Nhập để tải file có khóa riêng tư lên.</p>" +
                "<p style='padding-top:3px'>Bước 2: Nhấn nút Tải File lên để tải file chứa dữ liệu đơn hàng.</p>" +
                "<p style='padding-top:3px'>Bước 3: Nhấn nút Ký để thực hiện ký nội dung đơn hàng.</p>" +
                "<p style='padding-top:3px'>Bước 4: Nhấn nút Lưu Chữ ký và chọn thư mục để thực hiện </p>" +
                "<p style='padding-left:40px'>lưu file chữ ký về máy. </p>" +
                " </br> </br> " +
                "<p style='text-align:center; padding-top:20px; color:blue; font-size:15px'>Chúc bạn thành công!</p></html>";

        // Tạo hộp thoại tùy chỉnh (JDialog)
        JDialog dialog = new JDialog(this, "Hướng dẫn sử dụng", true);
        dialog.setLayout(new BorderLayout());

        // Tạo một JLabel để hiển thị hướng dẫn
        JLabel label = new JLabel(guideText, SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.TOP);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        // Thêm JLabel vào JDialog
        dialog.add(new JScrollPane(label), BorderLayout.CENTER);

        // Thiết lập kích thước và vị trí hộp thoại
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this); // Đặt dialog ở giữa JFrame

        // Hiển thị dialog
        dialog.setVisible(true);
    }

    // Hàm khởi chạy chương trinh
    public static void main(String[] args) {
        SwingUtilities.invokeLater(View::new);
    }
}

