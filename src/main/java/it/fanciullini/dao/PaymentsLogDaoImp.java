package it.fanciullini.dao;

import it.fanciullini.model.PaymentsLog;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class PaymentsLogDaoImp implements PaymentsLogDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(PaymentsLog paymentsLog) {
        sessionFactory.getCurrentSession().save(paymentsLog);
    }

    @Override
    public List<PaymentsLog> list() {
        @SuppressWarnings("unchecked")
        TypedQuery<PaymentsLog> query = sessionFactory.getCurrentSession().createQuery("from PaymentsLog ");
        return query.getResultList();
    }
}
