import com.mongodb.client.*;
import org.bson.Document;

import java.util.Scanner;

public class RealTimeCustomerFeedbackAggregator {

    // Node for Doubly Linked List
    static class FeedbackNode {
        String feedbackId;
        String customerName;
        String feedbackText;
        int rating;
        FeedbackNode prev, next;

        FeedbackNode(String feedbackId, String customerName, String feedbackText, int rating) {
            this.feedbackId = feedbackId;
            this.customerName = customerName;
            this.feedbackText = feedbackText;
            this.rating = rating;
        }
    }

    // Doubly Linked List with MongoDB Integration
    static class FeedbackDLL {
        FeedbackNode head, tail;
        MongoCollection<Document> collection;

        FeedbackDLL(MongoCollection<Document> collection) {
            this.collection = collection;
        }

        // Load feedback from database into DLL
        void loadFromDatabase() {
            FindIterable<Document> docs = collection.find();
            for (Document doc : docs) {
                String id = doc.getString("feedbackId");
                String name = doc.getString("customerName");
                String feedback = doc.getString("feedbackText");
                int rating = parseInteger(doc.get("rating"));

                FeedbackNode newNode = new FeedbackNode(id, name, feedback, rating);
                if (head == null) {
                    head = tail = newNode;
                } else {
                    tail.next = newNode;
                    newNode.prev = tail;
                    tail = newNode;
                }
            }
        }

        // Safe integer parsing
        private int parseInteger(Object obj) {
            if (obj instanceof Integer) return (Integer) obj;
            else if (obj instanceof String) return Integer.parseInt((String) obj);
            else return 0;
        }

        // Add feedback
        void addFeedback(String feedbackId, String customerName, String feedbackText, int rating) {
            FeedbackNode newNode = new FeedbackNode(feedbackId, customerName, feedbackText, rating);
            if (head == null) {
                head = tail = newNode;
            } else {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            }

            Document doc = new Document("feedbackId", feedbackId)
                    .append("customerName", customerName)
                    .append("feedbackText", feedbackText)
                    .append("rating", rating);

            collection.insertOne(doc);
            System.out.println("✅ Feedback added successfully!");
        }

        // Display all feedback
        void displayAllFeedback() {
            if (head == null) {
                System.out.println("No feedback found.");
                return;
            }

            FeedbackNode current = head;
            System.out.println("\n--- All Customer Feedback ---");
            while (current != null) {
                System.out.println("ID: " + current.feedbackId +
                        " | Name: " + current.customerName +
                        " | Rating: " + current.rating +
                        "\nFeedback: " + current.feedbackText);
                System.out.println("----------------------------------------");
                current = current.next;
            }
        }

        // Search feedback by ID
        FeedbackNode searchFeedback(String feedbackId) {
            FeedbackNode current = head;
            while (current != null) {
                if (current.feedbackId.equals(feedbackId)) return current;
                current = current.next;
            }
            return null;
        }

        // Update feedback
        void updateFeedback(String feedbackId, String newText, int newRating) {
            FeedbackNode node = searchFeedback(feedbackId);
            if (node != null) {
                node.feedbackText = newText;
                node.rating = newRating;

                collection.updateOne(
                        new Document("feedbackId", feedbackId),
                        new Document("$set", new Document("feedbackText", newText)
                                .append("rating", newRating))
                );
                System.out.println("✅ Feedback updated successfully!");
            } else {
                System.out.println("❌ Feedback not found.");
            }
        }

        // Delete feedback
        void deleteFeedback(String feedbackId) {
            FeedbackNode node = searchFeedback(feedbackId);
            if (node != null) {
                if (node.prev != null) node.prev.next = node.next;
                if (node.next != null) node.next.prev = node.prev;
                if (node == head) head = node.next;
                if (node == tail) tail = node.prev;

                collection.deleteOne(new Document("feedbackId", feedbackId));
                System.out.println("✅ Feedback deleted successfully!");
            } else {
                System.out.println("❌ Feedback not found.");
            }
        }

        // Calculate average rating (aggregator)
        void calculateAverageRating() {
            if (head == null) {
                System.out.println("No feedback available for aggregation.");
                return;
            }

            double sum = 0;
            int count = 0;
            FeedbackNode current = head;
            while (current != null) {
                sum += current.rating;
                count++;
                current = current.next;
            }

            double avg = sum / count;
            System.out.printf("⭐ Average Rating: %.2f (%d feedback entries)%n", avg, count);
        }
    }

    // Main method
    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("customer_feedback_db");
        MongoCollection<Document> collection = database.getCollection("feedback");

        FeedbackDLL feedbackList = new FeedbackDLL(collection);
        feedbackList.loadFromDatabase();

        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n--- Real-Time Customer Feedback Aggregator ---");
            System.out.println("1. Add Feedback");
            System.out.println("2. Display All Feedback");
            System.out.println("3. Search Feedback by ID");
            System.out.println("4. Update Feedback");
            System.out.println("5. Delete Feedback");
            System.out.println("6. Calculate Average Rating");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            while (!sc.hasNextInt()) {
                System.out.print("Invalid input. Enter a number: ");
                sc.next();
            }

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Enter Feedback ID: ");
                    String id = sc.nextLine();
                    System.out.print("Enter Customer Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Feedback Text: ");
                    String feedback = sc.nextLine();
                    System.out.print("Enter Rating (1-5): ");
                    int rating = sc.nextInt();
                    sc.nextLine();
                    feedbackList.addFeedback(id, name, feedback, rating);
                }
                case 2 -> feedbackList.displayAllFeedback();
                case 3 -> {
                    System.out.print("Enter Feedback ID to search: ");
                    String id = sc.nextLine();
                    FeedbackNode f = feedbackList.searchFeedback(id);
                    if (f != null)
                        System.out.println("✅ Found Feedback:\nCustomer: " + f.customerName +
                                "\nRating: " + f.rating + "\nFeedback: " + f.feedbackText);
                    else
                        System.out.println("❌ Feedback not found.");
                }
                case 4 -> {
                    System.out.print("Enter Feedback ID to update: ");
                    String id = sc.nextLine();
                    System.out.print("Enter new feedback text: ");
                    String newText = sc.nextLine();
                    System.out.print("Enter new rating (1-5): ");
                    int newRating = sc.nextInt();
                    sc.nextLine();
                    feedbackList.updateFeedback(id, newText, newRating);
                }
                case 5 -> {
                    System.out.print("Enter Feedback ID to delete: ");
                    String id = sc.nextLine();
                    feedbackList.deleteFeedback(id);
                }
                case 6 -> feedbackList.calculateAverageRating();
                case 7 -> System.out.println("👋 Exiting...");
                default -> System.out.println("Invalid choice. Try again!");
            }
        } while (choice != 7);

        mongoClient.close();
        sc.close();
    }
}
