package com.nan.myschool.gui;

import com.nan.myschool.entity.Enrollment;
import com.nan.myschool.entity.Pet;
import com.nan.myschool.service.EnrollmentsService;
import com.nan.myschool.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
        loadPets();  // 自动加载
    }

    private void initializeGUI() {
        setTitle("宠物档案查看器");
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

        JLabel selectLabel = new JLabel("选择宠物：");
        selectLabel.setFont(new Font("微软雅黑", Font.BOLD, 15));
        selectLabel.setForeground(Color.WHITE);

        petSelector = new JComboBox<>();
        petSelector.setPreferredSize(new Dimension(250, 32));
        petSelector.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        petSelector.addActionListener(e -> displayPetProfile());

        // 添加刷新按钮
        refreshButton = new JButton("刷新");
        refreshButton.setFont(new Font("微软雅黑", Font.PLAIN, 13));
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
                "宠物信息",
                0, 0,
                new Font("微软雅黑", Font.BOLD, 14),
                new Color(52, 152, 219)
        ));

        // 照片区域
        photoPanel = new JPanel();
        photoPanel.setPreferredSize(new Dimension(200, 200));
        photoPanel.setBackground(Color.WHITE);

        // 信息面板
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        nameLabel = createInfoLabel("", new Font("微软雅黑", Font.BOLD, 20));
        breedLabel = createInfoLabel("", new Font("微软雅黑", Font.PLAIN, 14));
        ageLabel = createInfoLabel("", new Font("微软雅黑", Font.PLAIN, 14));
        temperamentLabel = createInfoLabel("", new Font("微软雅黑", Font.PLAIN, 14));
        ownerLabel = createInfoLabel("", new Font("微软雅黑", Font.PLAIN, 14));
        contactLabel = createInfoLabel("", new Font("微软雅黑", Font.PLAIN, 14));

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

        // 训练进度条
        JPanel progressPanel = new JPanel(new BorderLayout(5, 5));
        progressPanel.setBorder(BorderFactory.createTitledBorder("总体训练进度"));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setPreferredSize(new Dimension(180, 28));
        progressBar.setForeground(new Color(46, 204, 113));
        progressBar.setFont(new Font("微软雅黑", Font.BOLD, 13));

        statsLabel = new JLabel("完成 0/0 门课程");
        statsLabel.setFont(new Font("微软雅黑", Font.PLAIN, 13));
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
                "训练历史记录",
                0, 0,
                new Font("微软雅黑", Font.BOLD, 14),
                new Color(46, 204, 113)
        ));

        String[] columns = {"课程名称", "训练师", "状态", "评级", "进度备注"};
        historyTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        historyTable = new JTable(historyTableModel);
        historyTable.setRowHeight(35);
        historyTable.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        historyTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 12));
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

        // 更新宠物信息
        nameLabel.setText(selectedPet.getName());
        breedLabel.setText("品种: " + selectedPet.getBreed() + " (" + selectedPet.getSpecies() + ")");
        ageLabel.setText("年龄: " + selectedPet.getAgeInMonths() + " 个月");
        temperamentLabel.setText("性格: " + selectedPet.getTemperament());
        ownerLabel.setText("主人: " + selectedPet.getOwnerName());
        contactLabel.setText("联系: " + selectedPet.getOwnerContact());

        loadTrainingHistory(selectedPet);
        updatePhotoPanel(selectedPet.getSpecies());
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

        // 更新进度条
        if (totalCount > 0) {
            int progress = (completedCount * 100) / totalCount;
            progressBar.setValue(progress);
            progressBar.setString(progress + "%");
            statsLabel.setText("完成 " + completedCount + "/" + totalCount + " 门课程");
        } else {
            progressBar.setValue(0);
            progressBar.setString("0%");
            statsLabel.setText("尚未报名任何课程");
        }
    }

    private void updatePhotoPanel(String species) {
        photoPanel.removeAll();

        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // 根据物种选择颜色
                Color bgColor = switch (species) {
                    case "狗" -> new Color(52, 152, 219);
                    case "猫" -> new Color(230, 126, 34);
                    case "兔子" -> new Color(231, 76, 160);
                    default -> new Color(149, 165, 166);
                };

                // 背景圆
                g2d.setColor(new Color(bgColor.getRed(), bgColor.getGreen(), bgColor.getBlue(), 50));
                g2d.fillRoundRect(10, 10, 180, 180, 90, 90);

                // 主圆
                g2d.setColor(bgColor);
                g2d.fillOval(50, 50, 100, 100);

                // 文字标签
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("微软雅黑", Font.BOLD, 16));
                String text = switch (species) {
                    case "狗" -> "DOG";
                    case "猫" -> "CAT";
                    case "兔子" -> "RABBIT";
                    default -> "PET";
                };

                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                g2d.drawString(text, 100 - textWidth/2, 105);
            }
        };

        iconPanel.setPreferredSize(new Dimension(200, 200));
        iconPanel.setBackground(Color.WHITE);
        photoPanel.add(iconPanel);
        photoPanel.revalidate();
        photoPanel.repaint();
    }

    private String getStatusText(String status) {
        return switch (status) {
            case "Completed" -> "[✓] 已完成";
            case "InProgress" -> "[>] 进行中";
            case "Dropped" -> "[X] 已退训";
            default -> "[ ] 未知";
        };
    }

    private String getRatingText(String rating) {
        if (rating == null) return "[ ] 未评级";
        return switch (rating) {
            case "Excellent" -> "[★★★] " + rating;
            case "Good" -> "[★★] " + rating;
            case "Fair" -> "[★] " + rating;
            case "NeedsWork" -> "[!] " + rating;
            default -> "[ ] " + rating;
        };
    }
}