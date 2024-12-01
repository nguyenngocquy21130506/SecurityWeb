import Algorithm.DigitalSignature;
import Algorithm.RSA;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

public class View extends JFrame {

    JPanel topBar, sidebar, panel2key;
    JLabel nameProgress;
    JTextArea message, signedMessage, result, guideMode, publicKey, privateKey;
    JButton generateKeyPairButton, loadPublicKeyButton, loadPrivateKeyButton,
            savePublicKeyButton, savePrivateKeyButton, loadSignature, saveSignature;
    JScrollPane scrollPaneMessage;
    JComboBox dropdownModeSignature;

    public View() {
        setTitle("Chữ ký điện tử");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);

        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        add(progressBar);

        // Chứa tên chương trình
        topBar = new JPanel();
        topBar.setBackground(Color.DARK_GRAY);
        topBar.setBounds(0, 0, 800, 50);
        topBar.setLayout(null);

        JLabel logo1 = new JLabel();
        ImageIcon iconLogo = new ImageIcon(View.class.getResource("/imgs/encrypted.png"));
        Image imageLogo = iconLogo.getImage();
        Image scaledLogo = imageLogo.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Icon scaledIcoLogo = new ImageIcon(scaledLogo);
        logo1.setIcon(scaledIcoLogo);
        logo1.setBounds(50, 1, 60, 50);
        JLabel logo2 = new JLabel();
        logo2.setIcon(scaledIcoLogo);
        logo2.setBounds(690, 1, 60, 50);
        topBar.add(logo1);
        topBar.add(logo2);

        nameProgress = new JLabel(" CHỮ KÝ ĐIỆN TỬ ");
        nameProgress.setForeground(Color.WHITE);
        nameProgress.setBounds(100, 2, 800, 50);
        nameProgress.setFont(new Font("Arial", Font.BOLD, 25));
        topBar.add(nameProgress);
        add(topBar);

        JLabel arrow = new JLabel();
        ImageIcon iconArrow = new ImageIcon(View.class.getResource("/imgs/next.png"));
        Image imageArrow = iconArrow.getImage();
        Image scaledArrow = imageArrow.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        Icon scaledIcoArrow = new ImageIcon(scaledArrow);
        arrow.setIcon(scaledIcoArrow);
        arrow.setBounds(265, 270, 30, 30);
        add(arrow);

        // Nút Copy ở đầu vào
        JButton copyMessageButton = new JButton();
        ImageIcon icon = new ImageIcon(View.class.getResource("/imgs/copy.png"));
        Image originalImage = icon.getImage();
        Image scaledImage = originalImage.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        Icon scaledIcon = new ImageIcon(scaledImage);
        copyMessageButton.setIcon(scaledIcon);
        copyMessageButton.setBounds(235, 430, 30, 30);
        copyMessageButton.setToolTipText("Sao chép nội dung thông điệp");
        add(copyMessageButton);

        // Nội dung đầu vào
        message = new JTextArea();
        message.setLineWrap(true);
        message.setWrapStyleWord(true);
        scrollPaneMessage = new JScrollPane(message);
        scrollPaneMessage.setBorder(BorderFactory.createTitledBorder("Dữ liệu đơn hàng"));
        scrollPaneMessage.setBounds(20, 130, 250, 150);
        add(scrollPaneMessage);

        // Chữ ký điện tử
        signedMessage = new JTextArea();
        signedMessage.setLineWrap(true);
        signedMessage.setWrapStyleWord(true);
        JScrollPane scrollPaneSignedMessage = new JScrollPane(signedMessage);
        scrollPaneSignedMessage.setBorder(BorderFactory.createTitledBorder("Chữ ký"));
        scrollPaneSignedMessage.setBounds(20, 280, 250, 150);
        add(scrollPaneSignedMessage);

        // Chọn giải thuật cho chữ ký điện tử
        String[] modesSignature = {"MD2", "MD5", "SHA-1", "SHA-256", "SHA-512"};
        dropdownModeSignature = new JComboBox<>(modesSignature);
        dropdownModeSignature.setBounds(20, 430, 100, 30);
        add(dropdownModeSignature);

        // Nút Tải chữ ký lên
        loadSignature = new JButton("Nhập");
        loadSignature.setToolTipText("Tải file chứa chữ ký lên");
        loadSignature.setBounds(125, 430, 100, 30);
        add(loadSignature);

        // Nút Lưu chữ ký
        saveSignature = new JButton("Lưu Chữ Ký");
        saveSignature.setBounds(290, 430, 200, 30);
        add(saveSignature);

        // Nút Copy kết quả
        JButton copyResultButton = new JButton();
        copyResultButton.setIcon(scaledIcon);
        copyResultButton.setBounds(505, 430, 30, 30);
        copyResultButton.setToolTipText("Sao chép nội dung kết quả");
        add(copyResultButton);

        // Kết quả
        result = new JTextArea();
        result.setLineWrap(true);
        result.setWrapStyleWord(true);
        result.setEditable(false);
        JScrollPane scrollPaneResult = new JScrollPane(result);
        scrollPaneResult.setBorder(BorderFactory.createTitledBorder("Chữ ký"));
        scrollPaneResult.setBounds(290, 130, 250, 300);
        add(scrollPaneResult);

        // ActionListener cho nút Copy nội dung
        copyMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(message.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            }
        });

        // ActionListener cho nút Copy kết quả
        copyResultButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection stringSelection = new StringSelection(result.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            }
        });

        // Nút mã hóa
        JButton buttonEncrypt = new JButton("Ký");
        buttonEncrypt.setBounds(90, 490, 100, 30);
        buttonEncrypt.setToolTipText("Ký dữ liệu đơn hàng");
        add(buttonEncrypt);

        // Nút Load File (khởi tạo nhưng không hiển thị vì mặc định là văn bản)
        JButton loadFileButton = new JButton("Tải file lên");
        loadFileButton.setBounds(225, 490, 100, 30);
        add(loadFileButton);

        // Nút giải mã
        JButton buttonDecrypt = new JButton("Xác thực");
        buttonDecrypt.setBounds(360, 490, 100, 30);
        buttonDecrypt.setToolTipText("Giải mã thông điệp");
        add(buttonDecrypt);

        // Danh cho cac giai thuat dung 2 khoa Public key va private key
        panel2key = new JPanel(null);
        panel2key.setBounds(560, 200, 240, 280);
        panel2key.setBackground(Color.LIGHT_GRAY);

        publicKey = new JTextArea();
        publicKey.setLineWrap(true);
        publicKey.setWrapStyleWord(true);
        JScrollPane publicKeyScrollPane = new JScrollPane(publicKey);
        publicKeyScrollPane.setBackground(Color.LIGHT_GRAY);
        publicKeyScrollPane.setBounds(35, 0, 160, 80);
        publicKeyScrollPane.setBorder(BorderFactory.createTitledBorder("Khóa công khai"));
        panel2key.add(publicKeyScrollPane);

        loadPublicKeyButton = new JButton("Nhập");
        loadPublicKeyButton.setBounds(45, 80, 70, 30);
        panel2key.add(loadPublicKeyButton);

        savePublicKeyButton = new JButton("Lưu");
        savePublicKeyButton.setBounds(115, 80, 70, 30);
        panel2key.add(savePublicKeyButton);

        privateKey = new JTextArea();
        privateKey.setLineWrap(true);
        privateKey.setWrapStyleWord(true);
        JScrollPane privateKeyScrollPane = new JScrollPane(privateKey);
        privateKeyScrollPane.setBackground(Color.LIGHT_GRAY);
        privateKeyScrollPane.setBounds(35, 115, 160, 80);
        privateKeyScrollPane.setBorder(BorderFactory.createTitledBorder("Khóa riêng tư"));
        panel2key.add(privateKeyScrollPane);

        loadPrivateKeyButton = new JButton("Nhập");
        loadPrivateKeyButton.setBounds(45, 195, 70, 30);
        panel2key.add(loadPrivateKeyButton);

        savePrivateKeyButton = new JButton("Lưu");
        savePrivateKeyButton.setBounds(115, 195, 70, 30);
        panel2key.add(savePrivateKeyButton);

        generateKeyPairButton = new JButton("Tạo khóa");
        generateKeyPairButton.setBounds(45, 230, 140, 30);
        panel2key.add(generateKeyPairButton);

        add(panel2key);

        // Thanh ben trai, khu vực xử lý key
        sidebar = new JPanel(null);
        sidebar.setBackground(Color.LIGHT_GRAY);
        sidebar.setBounds(560, 0, 240, 600);
        add(sidebar);

        JLabel sidebarButton1 = new JLabel("Khóa");
        sidebarButton1.setForeground(Color.BLACK);
        sidebarButton1.setFont(new Font("Arial", Font.BOLD, 35));
        sidebarButton1.setBounds(45, 70, 140, 35);
        sidebar.add(sidebarButton1);

        JLabel key = new JLabel();
        ImageIcon iconKey = new ImageIcon(View.class.getResource("/imgs/encrypted-data.png"));
        Image imageKey = iconKey.getImage();
        Image scaledKey = imageKey.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Icon scaledIcoKey = new ImageIcon(scaledKey);
        key.setIcon(scaledIcoKey);
        key.setBounds(140, 35, 100, 100);
        sidebar.add(key);

        JLabel guide = new JLabel();
        ImageIcon iconGuide = new ImageIcon(View.class.getResource("/imgs/guidebook.png"));
        Image imageGuide = iconGuide.getImage();
        Image scaledGuide = imageGuide.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Icon scaledIcoGuide = new ImageIcon(scaledGuide);
        guide.setIcon(scaledIcoGuide);
        guide.setBounds(45, 140, 50, 50);
        sidebar.add(guide);

        // Chua thong tin gui y cho tung loai giai thuat
        guideMode = new JTextArea();
        guideMode.setBounds(95, 140, 90, 50);
        guideMode.setBackground(null);
        guideMode.setForeground(Color.BLACK);
        guideMode.setLineWrap(true);
        guideMode.setWrapStyleWord(true);
        guideMode.setText("Khóa có độ dài 8 ký tự");
        guideMode.setEditable(false);
        sidebar.add(guideMode);

        setVisible(true);

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
                }
            }
        });

        // Su kien load file chu ky dien tu
        loadSignature.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();

                    try {
                        List<String> lines = Files.readAllLines(Paths.get(filePath));
                        if (lines.size() > 0) {
                            StringBuilder signatureContent = new StringBuilder();
                            for (String line : lines) {
                                signatureContent.append(line).append("\n");
                            }
                            signedMessage.setText(signatureContent.toString().trim());
                        } else {
                            JOptionPane.showMessageDialog(null, "File không chứa dữ liệu hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi đọc file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
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
                        JOptionPane.showMessageDialog(null, "Chữ ký đã được lưu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi lưu file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Them xu ly cho nut tao khoa cho cac giai thuat dung 2 khoa Public key va private key
        generateKeyPairButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RSA rsa = null;
                try {
                    rsa = new RSA();
                    String publicKeyBase64 = Base64.getEncoder().encodeToString(rsa.getPublicKey().getEncoded());
                    publicKey.setText(publicKeyBase64);
                    String privateKeyBase64 = Base64.getEncoder().encodeToString(rsa.getPrivateKey().getEncoded());
                    privateKey.setText(privateKeyBase64);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        // Them xu ly cho nut Load public key
        loadPublicKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();

                    try {
                        List<String> lines = Files.readAllLines(Paths.get(filePath));
                        if (lines.size() > 0 && lines.get(0).equals("-----BEGIN PUBLIC KEY-----")) {
                            StringBuilder publicKeyContent = new StringBuilder();
                            for (int i = 1; i < lines.size(); i++) {
                                publicKeyContent.append(lines.get(i)).append("\n");
                            }
                            publicKey.setText(publicKeyContent.toString().trim());
                        } else {
                            JOptionPane.showMessageDialog(null, "File không chứa khóa hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi đọc file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Them xu ly cho nut Save Public key
        savePublicKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String publicKeyText = publicKey.getText();
                if (publicKeyText == null || publicKeyText.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Chữ ký không thể trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String formattedPublicKey = "-----BEGIN PUBLIC KEY-----\n" + publicKeyText;
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();

                    try {
                        FileWriter writer = new FileWriter(filePath);
                        writer.write(formattedPublicKey);
                        writer.close();
                        JOptionPane.showMessageDialog(null, "Chữ ký đã được lưu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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
                            StringBuilder publicKeyContent = new StringBuilder();
                            for (int i = 1; i < lines.size(); i++) {
                                publicKeyContent.append(lines.get(i)).append("\n");
                            }
                            privateKey.setText(publicKeyContent.toString().trim());
                        } else {
                            JOptionPane.showMessageDialog(null, "File không chứa khóa hợp lệ.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi đọc file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        // Them xu ly cho nut Save Private Key
        savePrivateKeyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String privateKeyText = privateKey.getText();
                if (privateKeyText == null || privateKeyText.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Chữ ký không thể trống.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String formattedPrivateKey = "-----BEGIN PRIVATE KEY-----\n" + privateKeyText;
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showSaveDialog(null);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String filePath = selectedFile.getAbsolutePath();

                    try {
                        FileWriter writer = new FileWriter(filePath);
                        writer.write(formattedPrivateKey);
                        writer.close();
                        JOptionPane.showMessageDialog(null, "Chữ ký đã được lưu.", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(null, "Lỗi khi lưu file: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Them xu ly cho nut Encrypt
        buttonEncrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result.setText("");
                signedMessage.setText("");

                if (message.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập thông điệp cần mã hóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // Hiển thị progress bar
                progressBar.setVisible(true);
                progressBar.setBounds(0, 550, 560, 50);
                progressBar.setIndeterminate(false); // Chuyển sang chế độ xác định
                progressBar.setMaximum(20); // Giả sử có 20 bước

                saveSignature.setEnabled(true);

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

        // Them xu ly cho nut Decrypt
        buttonDecrypt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result.setText("");
                if (message.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Vui lòng nhập thông điệp cần mã hóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                progressBar.setVisible(true);
                progressBar.setBounds(0, 550, 560, 50);
                progressBar.setIndeterminate(false);
                progressBar.setMaximum(20);

                saveSignature.setEnabled(false);

                // Sử dụng SwingWorker để xử lý mã hóa
                SwingWorker<Void, Integer> worker = new SwingWorker<Void, Integer>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        verifySignatureFile();
                        for (int i = 0; i <= 20; i++) {
                            Thread.sleep(10);
                            publish(i);
                        }
                        return null;
                    }

                    @Override
                    protected void process(java.util.List<Integer> chunks) {
                        for (Integer progress : chunks) {
                            progressBar.setIndeterminate(false);
                            progressBar.setValue(progress);
                        }
                    }

                    @Override
                    protected void done() {
                        progressBar.setVisible(false);
                        if (result.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Giải mã không thành công.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        } else if (result.getText().isEmpty()) {
                            JOptionPane.showMessageDialog(null, "Xác thực chữ ký không thành công.", "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                };
                worker.execute();
            }
        });
    }

    // Ham ky chu ky dien tu dang van bang
    private void signatureText() {
        try {
            DigitalSignature signature = new DigitalSignature();
            if (privateKey.getText().isEmpty() || publicKey.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đúng định dạng khóa", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            signature.setPrivateKey(Base64.getDecoder().decode(privateKey.getText()));
            signature.setPublicKey(Base64.getDecoder().decode(publicKey.getText()));
            String modeHash = (String) dropdownModeSignature.getSelectedItem();
            String hashMessage = "";
            switch (modeHash) {
                case "MD2":
                    hashMessage = signature.signMessage(message.getText(), signature.getPrivateKey(), "MD2withRSA");
                    break;
                case "MD5":
                    hashMessage = signature.signMessage(message.getText(), signature.getPrivateKey(), "MD5withRSA");
                    break;
                case "SHA-1":
                    hashMessage = signature.signMessage(message.getText(), signature.getPrivateKey(), "SHA1withRSA");
                    break;
                case "SHA-256":
                    hashMessage = signature.signMessage(message.getText(), signature.getPrivateKey(), "SHA256withRSA");
                    break;
                case "SHA-512":
                    hashMessage = signature.signMessage(message.getText(), signature.getPrivateKey(), "SHA512withRSA");
                    break;
            }
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

    // Ham ky chu ky dien tu dang file
    private void signatureFile() {
        try {
            DigitalSignature signature = new DigitalSignature();
            if (privateKey.getText().isEmpty() || publicKey.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đúng định dạng khóa", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            signature.setPrivateKey(Base64.getDecoder().decode(privateKey.getText()));
            signature.setPublicKey(Base64.getDecoder().decode(publicKey.getText()));
            String modeHash = (String) dropdownModeSignature.getSelectedItem();
            String hashMessage = "";
            switch (modeHash) {
                case "MD2":
                    hashMessage = signature.signFile(new File(message.getText()), signature.getPrivateKey(), "MD2withRSA");
                    break;
                case "MD5":
                    hashMessage = signature.signFile(new File(message.getText()), signature.getPrivateKey(), "MD5withRSA");
                    break;
                case "SHA-1":
                    hashMessage = signature.signFile(new File(message.getText()), signature.getPrivateKey(), "SHA1withRSA");
                    break;
                case "SHA-256":
                    hashMessage = signature.signFile(new File(message.getText()), signature.getPrivateKey(), "SHA256withRSA");
                    break;
                case "SHA-512":
                    hashMessage = signature.signFile(new File(message.getText()), signature.getPrivateKey(), "SHA512withRSA");
                    break;
            }
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

    // Ham xac thuc chu ky dien tu dang van bang
    private void verifySignatureText() {
        try {
            DigitalSignature signature = new DigitalSignature();
            if (privateKey.getText().isEmpty() || publicKey.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đúng định dạng khóa", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            signature.setPrivateKey(Base64.getDecoder().decode(privateKey.getText()));
            signature.setPublicKey(Base64.getDecoder().decode(publicKey.getText()));
            String modeHash = (String) dropdownModeSignature.getSelectedItem();
            boolean isVerify = false;
            switch (modeHash) {
                case "MD2":
                    isVerify = signature.verifySignature(message.getText(), signedMessage.getText(), signature.getPublicKey(), "MD2withRSA");
                    break;
                case "MD5":
                    isVerify = signature.verifySignature(message.getText(), signedMessage.getText(), signature.getPublicKey(), "MD5withRSA");
                    break;
                case "SHA-1":
                    isVerify = signature.verifySignature(message.getText(), signedMessage.getText(), signature.getPublicKey(), "SHA1withRSA");
                    break;
                case "SHA-256":
                    isVerify = signature.verifySignature(message.getText(), signedMessage.getText(), signature.getPublicKey(), "SHA256withRSA");
                    break;
                case "SHA-512":
                    isVerify = signature.verifySignature(message.getText(), signedMessage.getText(), signature.getPublicKey(), "SHA512withRSA");
                    break;
            }
            if (isVerify) {
                result.setText("Chữ ký hợp lệ");
                result.setBackground(Color.GREEN);
                Timer timer = new Timer(2000, e -> {
                    result.setBackground(Color.white);
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                result.setText("Chữ ký không hợp lệ hoặc nội dung đã bị thay đổi");
                result.setBackground(Color.RED);
                Timer timer = new Timer(2000, e -> {
                    result.setBackground(Color.white);
                });
                timer.setRepeats(false);
                timer.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    /*--------------------------------------Decrypt File--------------------------------------*/

    // Hàm xác thực chữ ký dien tu dang file
    private void verifySignatureFile() {
        try {
            DigitalSignature signature = new DigitalSignature();
            if (privateKey.getText().isEmpty() || publicKey.getText().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Vui lòng nhập đúng định dạng khóa", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
            signature.setPrivateKey(Base64.getDecoder().decode(privateKey.getText()));
            signature.setPublicKey(Base64.getDecoder().decode(publicKey.getText()));
            String modeHash = (String) dropdownModeSignature.getSelectedItem();
            boolean isVerify = false;
            switch (modeHash) {
                case "MD2":
                    isVerify = signature.verifyFileSignature(new File(message.getText()), signedMessage.getText(), signature.getPublicKey(), "MD2withRSA");
                    break;
                case "MD5":
                    isVerify = signature.verifyFileSignature(new File(message.getText()), signedMessage.getText(), signature.getPublicKey(), "MD5withRSA");
                    break;
                case "SHA-1":
                    isVerify = signature.verifyFileSignature(new File(message.getText()), signedMessage.getText(), signature.getPublicKey(), "SHA1withRSA");
                    break;
                case "SHA-256":
                    isVerify = signature.verifyFileSignature(new File(message.getText()), signedMessage.getText(), signature.getPublicKey(), "SHA256withRSA");
                    break;
                case "SHA-512":
                    isVerify = signature.verifyFileSignature(new File(message.getText()), signedMessage.getText(), signature.getPublicKey(), "SHA512withRSA");
                    break;
            }
            if (isVerify) {
                result.setText("Chữ ký hợp lệ");
                result.setBackground(Color.GREEN);
                Timer timer = new Timer(2000, e -> {
                    result.setBackground(Color.white);
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                result.setText("Chữ ký không hợp lệ hoặc nội dung đã bị thay đổi");
                result.setBackground(Color.RED);
                Timer timer = new Timer(2000, e -> {
                    result.setBackground(Color.white);
                });
                timer.setRepeats(false);
                timer.start();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Hàm khởi chạy chương trinh
    public static void main(String[] args) {
        SwingUtilities.invokeLater(View::new);
    }
}

