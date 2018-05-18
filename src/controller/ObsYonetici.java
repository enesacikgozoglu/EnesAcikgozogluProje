/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.util.List;
import java.util.Vector;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import model.Ogrenci;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author rafadan
 */
public class ObsYonetici {
    
    private JTable ogrenciTablo;
    private final static String SORGU_KALIP = "from Ogrenci o";
    private Session session;
    private Vector<String> sutunlar = new Vector<>();
    private Vector<Object> satir;
    private DefaultTableModel model;
    
    public ObsYonetici(JTable ogrenciTablo) {
        this.ogrenciTablo = ogrenciTablo;
        sutunlar.add("Öğrenci ID");
        sutunlar.add("Öğrenci No");
        sutunlar.add("Ad Soyad");
        sutunlar.add("Şehir");
        sutunlar.add("Tel No");
        model = (DefaultTableModel) ogrenciTablo.getModel();
        model.setColumnIdentifiers(sutunlar);
    }
    
    public void ogrenciGetir(String aranan, String filtre) {
        String sorgu = "";
        if (filtre.equalsIgnoreCase("ogrencino")) {
            sorgu = SORGU_KALIP + " where o.ogrencino like '%" + aranan + "%'";
        } else if (filtre.equalsIgnoreCase("adsoyad")) {
            sorgu = SORGU_KALIP + " where o.adsoyad like '%" + aranan + "%'";
        }
        session.beginTransaction();
        List ogrenciList = session.createQuery(sorgu).list();
        session.getTransaction().commit();
        //session.close();
        ogrenciGoster(ogrenciList);

    }
    
    public Ogrenci getOgrenciByNo(int ogrencino) {
        String sorgu = "";
        sorgu = SORGU_KALIP + " where o.ogrenciid=" + ogrencino;
        session.beginTransaction();
        Ogrenci gelenOgrenci = (Ogrenci) session.createQuery(sorgu).list().get(0);
        session.getTransaction().commit();
        //session.close();
        Ogrenci ogrenci = new Ogrenci();
        ogrenci.setOgrenciid(gelenOgrenci.getOgrenciid());
        ogrenci.setOgrencino(gelenOgrenci.getOgrencino());
        ogrenci.setAdsoyad(gelenOgrenci.getAdsoyad());
        ogrenci.setSehir(gelenOgrenci.getSehir());
        ogrenci.setTelno(gelenOgrenci.getTelno());

        return ogrenci;
    }
    
    public int getSelectedOgrenciid() {
        int index = this.ogrenciTablo.getSelectedRow();
        Object id = this.ogrenciTablo.getValueAt(index, 0);
        return (int) id;
    }

    public void ogrencileriListtele() {
        session.beginTransaction();
        List ogrencilerList = session.createQuery(SORGU_KALIP).list();
        session.getTransaction().commit();
        ogrenciGoster(ogrencilerList);
    }

    public void deleteOgrenciById(int ogrenciId) {
         session.beginTransaction();
         Ogrenci ogrenci = (Ogrenci)session.load(Ogrenci.class,ogrenciId);
    session.delete(ogrenci);
        session.getTransaction().commit();
    }

    public boolean insertOgrenci(String ogrencino, String adsoyad, String sehir, String telno) {

        try {
            session.beginTransaction();
            Ogrenci ogrenci = new Ogrenci();
            ogrenci.setOgrencino(ogrencino);
            ogrenci.setAdsoyad(adsoyad);
            ogrenci.setSehir(sehir);
            ogrenci.setTelno(telno);
            session.save(ogrenci);
            session.getTransaction().commit();
        } catch (HibernateException ex) {
            session.getTransaction().rollback();
            return false;
        } finally {
//          session.close();
        }
        return true;
    }

    public boolean guncelleOgrenciById(int id, String ogrencino, String adsoyad, String sehir, String telno) {

        try {
            Ogrenci ogrenci = (Ogrenci) session.get(Ogrenci.class, id);
            session.beginTransaction();
            ogrenci.setOgrencino(ogrencino);
            ogrenci.setAdsoyad(adsoyad);
            ogrenci.setSehir(sehir);
            ogrenci.setTelno(telno);
            session.update(ogrenci);
            session.getTransaction().commit();
        } catch (HibernateException ex) {
            session.getTransaction().rollback();
            return false;
        } finally {
            // session.close();
        }
        return true;
    }

    public void ac() {
        session = HibernateUtil.getSessionFactory().openSession();
    }

    public void kapat() {
        session.close();
    }

    private void ogrenciGoster(List<Ogrenci> ogrencilerList) {
        model.getDataVector().removeAllElements();
        for (Ogrenci gelenOgrenci : ogrencilerList) {
            satir = new Vector();
            satir.add(gelenOgrenci.getOgrenciid());
            satir.add(gelenOgrenci.getOgrencino());
            satir.add(gelenOgrenci.getAdsoyad());
            satir.add(gelenOgrenci.getSehir());
            satir.add(gelenOgrenci.getTelno());
            model.addRow(satir);
        }

    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
