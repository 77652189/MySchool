package com.nan.myschool.gui;

import com.nan.myschool.entity.Enrollment;
import com.nan.myschool.entity.Pet;
import com.nan.myschool.service.EnrollmentsService;
import com.nan.myschool.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.List;

@Component
public class PetProfileGUI extends JFrame {
    private final PetService petService;
    private final EnrollmentsService enrollmentsService;

    private JComboBox<Pet> petSelector;
    private JPanel photoPanel;
    private JLabel nameLabel;
    private JLabel breedLabel;
    private JLabel ageLabel;
    private JLabel temperamentLabel;
    private JLabel ownerLabel;
    private JLabel contactLabel;
    private JProgressBar progressBar;
    private JLabel statsLabel;
    private JButton refreshButton;

    private JTable historyTable;
    private DefaultTableModel historyTableModel;

    @Autowired
    public PetProfileGUI(PetService petService, EnrollmentsService enrollmentsService) {
        this.petService = petService;
        this.enrollmentsService = enrollmentsService;
        initializeGUI();
        loadPets();
    }

    private void initializeGUI() {
        setTitle("Pet Profile Viewer");
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(950, 650);
        setLayout(new BorderLayout(15, 15));

        ((JPanel)getContentPane()).setBorder(new EmptyBorder(15, 15, 15, 15));

        JPanel topPanel = createTopPanel();
        JPanel leftPanel = createPetInfoCard();
        JPanel rightPanel = createTrainingHistoryPanel();

        JPanel mainPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        add(topPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    private JPanel createTopPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(new Color(52, 73, 94));

        JLabel selectLabel = new JLabel("Select Pet:");
        selectLabel.setFont(new Font("Dialog", Font.BOLD, 15));
        selectLabel.setForeground(Color.WHITE);

        petSelector = new JComboBox<>();
        petSelector.setPreferredSize(new Dimension(250, 32));
        petSelector.setFont(new Font("Dialog", Font.PLAIN, 14));
        petSelector.addActionListener(e -> displayPetProfile());

        refreshButton = new JButton("Refresh");
        refreshButton.setFont(new Font("Dialog", Font.PLAIN, 13));
        refreshButton.setPreferredSize(new Dimension(80, 32));
        refreshButton.setBackground(new Color(46, 204, 113));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFocusPainted(false);
        refreshButton.setBorderPainted(false);
        refreshButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshButton.addActionListener(e -> loadPets());

        panel.add(selectLabel);
        panel.add(petSelector);
        panel.add(refreshButton);

        return panel;
    }

    private JPanel createPetInfoCard() {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                "Pet Information",
                0, 0,
                new Font("Dialog", Font.BOLD, 14),
                new Color(52, 152, 219)
        ));

        // Photo area
        photoPanel = new JPanel();
        photoPanel.setPreferredSize(new Dimension(200, 200));
        photoPanel.setBackground(Color.WHITE);

        // Information panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        nameLabel = createInfoLabel("", new Font("Dialog", Font.BOLD, 20));
        breedLabel = createInfoLabel("", new Font("Dialog", Font.PLAIN, 14));
        ageLabel = createInfoLabel("", new Font("Dialog", Font.PLAIN, 14));
        temperamentLabel = createInfoLabel("", new Font("Dialog", Font.PLAIN, 14));
        ownerLabel = createInfoLabel("", new Font("Dialog", Font.PLAIN, 14));
        contactLabel = createInfoLabel("", new Font("Dialog", Font.PLAIN, 14));

        infoPanel.add(nameLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(breedLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(ageLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(temperamentLabel);
        infoPanel.add(Box.createVerticalStrut(15));
        infoPanel.add(new JSeparator());
        infoPanel.add(Box.createVerticalStrut(10));
        infoPanel.add(ownerLabel);
        infoPanel.add(Box.createVerticalStrut(8));
        infoPanel.add(contactLabel);

        // Training progress bar
        JPanel progressPanel = new JPanel(new BorderLayout(5, 5));
        progressPanel.setBorder(BorderFactory.createTitledBorder("Overall Training Progress"));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(180, 28));
        progressBar.setForeground(new Color(46, 204, 113));
        progressBar.setFont(new Font("Dialog", Font.BOLD, 13));

        statsLabel = new JLabel("Completed 0/0 courses");
        statsLabel.setFont(new Font("Dialog", Font.PLAIN, 13));
        statsLabel.setHorizontalAlignment(SwingConstants.CENTER);

        progressPanel.add(progressBar, BorderLayout.CENTER);
        progressPanel.add(statsLabel, BorderLayout.SOUTH);

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.add(photoPanel, BorderLayout.NORTH);
        topSection.add(infoPanel, BorderLayout.CENTER);
        topSection.add(progressPanel, BorderLayout.SOUTH);

        card.add(topSection, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTrainingHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(46, 204, 113), 2),
                "Training History",
                0, 0,
                new Font("Dialog", Font.BOLD, 14),
                new Color(46, 204, 113)
        ));

        String[] columns = {"Course Name", "Trainer", "Status", "Rating", "Progress Notes"};
        historyTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        historyTable = new JTable(historyTableModel);
        historyTable.setRowHeight(35);
        historyTable.setFont(new Font("Dialog", Font.PLAIN, 13));
        historyTable.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));
        historyTable.getTableHeader().setBackground(new Color(46, 204, 113));
        historyTable.getTableHeader().setForeground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(historyTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JLabel createInfoLabel(String text, Font font) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        return label;
    }

    private void loadPets() {
        petSelector.removeAllItems();
        List<Pet> pets = petService.getAllPets();
        for (Pet pet : pets) {
            petSelector.addItem(pet);
        }

        if (!pets.isEmpty()) {
            displayPetProfile();
        }
    }

    private void displayPetProfile() {
        Pet selectedPet = (Pet) petSelector.getSelectedItem();
        if (selectedPet == null) return;

        // Update pet information
        nameLabel.setText(selectedPet.getName());
        breedLabel.setText("Breed: " + selectedPet.getBreed() + " (" + selectedPet.getSpecies() + ")");
        ageLabel.setText("Age: " + selectedPet.getAgeInMonths() + " months");
        temperamentLabel.setText("Temperament: " + selectedPet.getTemperament());
        ownerLabel.setText("Owner: " + selectedPet.getOwnerName());
        contactLabel.setText("Contact: " + selectedPet.getOwnerContact());

        loadTrainingHistory(selectedPet);
        updatePhotoPanel(selectedPet);
    }

    private void loadTrainingHistory(Pet pet) {
        historyTableModel.setRowCount(0);
        List<Enrollment> enrollments = enrollmentsService.getEnrollmentsByPet(pet);

        int completedCount = 0;
        int totalCount = enrollments.size();

        for (Enrollment enrollment : enrollments) {
            String statusText = getStatusText(enrollment.getStatus());
            String ratingText = getRatingText(enrollment.getRating());

            Object[] row = {
                    enrollment.getCourseSection().getCourse().getTitle(),
                    enrollment.getCourseSection().getTrainer().getName(),
                    statusText,
                    ratingText,
                    enrollment.getProgressNotes() != null ? enrollment.getProgressNotes() : "-"
            };
            historyTableModel.addRow(row);

            if ("Completed".equals(enrollment.getStatus())) {
                completedCount++;
            }
        }

        // Update progress bar
        if (totalCount > 0) {
            int progress = (completedCount * 100) / totalCount;
            progressBar.setValue(progress);
            progressBar.setString(progress + "%");
            statsLabel.setText("Completed " + completedCount + "/" + totalCount + " courses");
        } else {
            progressBar.setValue(0);
            progressBar.setString("0%");
            statsLabel.setText("Not enrolled in any courses");
        }
    }

    private void updatePhotoPanel(Pet pet) {
        photoPanel.removeAll();
        photoPanel.setLayout(new BorderLayout());

        String photoUrl = pet.getPhotoUrl();
        boolean imageLoaded = false;

        if (photoUrl != null && !photoUrl.isEmpty()) {
            try {
                // 从 classpath 加载图片（适用于 resources 文件夹）
                InputStream is = getClass().getResourceAsStream("/static" + photoUrl);

                if (is != null) {
                    BufferedImage originalImage = ImageIO.read(is);
                    if (originalImage != null) {
                        // 创建带圆角和边框的图片显示面板
                        JPanel imagePanel = new JPanel() {
                            @Override
                            protected void paintComponent(Graphics g) {
                                super.paintComponent(g);
                                Graphics2D g2d = (Graphics2D) g;
                                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                                g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

                                // 计算居中位置
                                int panelWidth = getWidth();
                                int panelHeight = getHeight();
                                int imageSize = 180;
                                int x = (panelWidth - imageSize) / 2;
                                int y = (panelHeight - imageSize) / 2;

                                // 绘制阴影
                                g2d.setColor(new Color(0, 0, 0, 30));
                                g2d.fillRoundRect(x + 3, y + 3, imageSize, imageSize, 20, 20);

                                // 绘制白色背景（带圆角）
                                g2d.setColor(Color.WHITE);
                                g2d.fillRoundRect(x, y, imageSize, imageSize, 20, 20);

                                // 设置裁剪区域为圆角矩形
                                g2d.setClip(new java.awt.geom.RoundRectangle2D.Float(x, y, imageSize, imageSize, 20, 20));

                                // 缩放并绘制图片，保持宽高比
                                int originalWidth = originalImage.getWidth();
                                int originalHeight = originalImage.getHeight();

                                double scale = Math.max(
                                        (double) imageSize / originalWidth,
                                        (double) imageSize / originalHeight
                                );

                                int scaledWidth = (int) (originalWidth * scale);
                                int scaledHeight = (int) (originalHeight * scale);

                                int imgX = x + (imageSize - scaledWidth) / 2;
                                int imgY = y + (imageSize - scaledHeight) / 2;

                                g2d.drawImage(originalImage, imgX, imgY, scaledWidth, scaledHeight, null);

                                // 重置裁剪区域
                                g2d.setClip(null);

                                // 绘制边框
                                g2d.setColor(new Color(52, 152, 219));
                                g2d.setStroke(new BasicStroke(3));
                                g2d.drawRoundRect(x, y, imageSize, imageSize, 20, 20);
                            }
                        };

                        imagePanel.setPreferredSize(new Dimension(200, 200));
                        imagePanel.setBackground(Color.WHITE);
                        photoPanel.add(imagePanel, BorderLayout.CENTER);

                        imageLoaded = true;
                    }
                    is.close();
                } else {
                    System.out.println("Image not found: /static" + photoUrl);
                }
            } catch (Exception e) {
                System.out.println("Failed to load image: " + photoUrl);
                e.printStackTrace();
            }
        }

        // 如果图片加载失败，显示默认图标
        if (!imageLoaded) {
            addDefaultIcon(photoPanel, pet.getSpecies());
        }

        photoPanel.revalidate();
        photoPanel.repaint();
    }

    private void addDefaultIcon(JPanel panel, String species) {
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Choose color based on species
                Color bgColor = switch (species) {
                    case "Dog", "狗" -> new Color(52, 152, 219);
                    case "Cat", "猫" -> new Color(230, 126, 34);
                    case "Rabbit", "兔子" -> new Color(231, 76, 160);
                    default -> new Color(149, 165, 166);
                };

                // Background circle
                g2d.setColor(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 50));
                g2d.fillRoundRect(10, 10, 180, 180, 90, 90);

                // Main circle
                g2d.setColor(bgColor);
                g2d.fillOval(50, 50, 100, 100);

                // Text label
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Dialog", Font.BOLD, 16));
                String text = switch (species) {
                    case "Dog", "狗" -> "DOG";
                    case "Cat", "猫" -> "CAT";
                    case "Rabbit", "兔子" -> "RABBIT";
                    default -> "PET";
                };

                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                g2d.drawString(text, 100 - textWidth/2, 105);
            }
        };

        iconPanel.setPreferredSize(new Dimension(200, 200));
        iconPanel.setBackground(Color.WHITE);
        panel.add(iconPanel);
    }

    private String getStatusText(String status) {
        return switch (status) {
            case "Completed" -> "[✓] Completed";
            case "InProgress" -> "[>] In Progress";
            case "Dropped" -> "[X] Dropped";
            default -> "[ ] Unknown";
        };
    }

    private String getRatingText(String rating) {
        if (rating == null) return "[ ] Not Rated";
        return switch (rating) {
            case "Excellent" -> "[★★★] " + rating;
            case "Good" -> "[★★] " + rating;
            case "Fair" -> "[★] " + rating;
            case "NeedsWork" -> "[!] " + rating;
            default -> "[ ] " + rating;
        };
    }
}