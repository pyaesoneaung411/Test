package com.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.List;
import java.util.Scanner;

public class StudentMain {
    private static EntityManagerFactory emf;

    public static void main(String[] args) {
        emf = Persistence.createEntityManagerFactory("jpatest");
        EntityManager em = emf.createEntityManager();
        Scanner scanner = new Scanner(System.in);

        try {
         
            em.getTransaction().begin();
            ForStudent student = new ForStudent();
            student.setName("John Doe");
            em.persist(student);
            em.getTransaction().commit();
            System.out.println("Inserted student with ID: " + student.getId());

          
            System.out.print("Enter student ID to update: ");
            int idToUpdate = scanner.nextInt();
            scanner.nextLine(); // consume newline
            System.out.print("Enter new name for student: ");
            String newName = scanner.nextLine();

            em.getTransaction().begin();
            ForStudent studentToUpdate = em.find(ForStudent.class, idToUpdate);
            if (studentToUpdate != null) {
                studentToUpdate.setName(newName);
                em.merge(studentToUpdate);
                em.getTransaction().commit();
                System.out.println("Updated student with ID: " + studentToUpdate.getId());
            } else {
                em.getTransaction().rollback();
                System.out.println("Student with ID " + idToUpdate + " not found.");
            }

          
            em.getTransaction().begin();
            List<ForStudent> students = em.createQuery("SELECT s FROM ForStudent s", ForStudent.class).getResultList();
            for (ForStudent s : students) {
                System.out.println("Student ID: " + s.getId() + ", Name: " + s.getName());
            }
            em.getTransaction().commit();

         
            System.out.print("Enter student ID to delete: ");
            int idToDelete = scanner.nextInt();
            em.getTransaction().begin();
            ForStudent studentToDelete = em.find(ForStudent.class, idToDelete);
            if (studentToDelete != null) {
                em.remove(studentToDelete);
                em.getTransaction().commit();
                System.out.println("Deleted student with ID: " + studentToDelete.getId());
            } else {
                em.getTransaction().rollback();
                System.out.println("Student with ID " + idToDelete + " not found.");
            }
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            emf.close();
            scanner.close();
        }
    }
}
