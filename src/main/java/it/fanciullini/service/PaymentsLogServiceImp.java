package it.fanciullini.dao;

import it.fanciullini.model.PaymentsLog;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.TypedQuery;
import java.util.List;

public class PaymentsLogDaoImp implements PaymentsLogDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void save(PaymentsLog paymentsLog) {
        sessionFactory.getCurrentSession().save(paymentsLog);
    }

    @Override
    public List<PaymentsLog> list() {
        TypedQuery<PaymentsLog> query = sessionFactory.getCurrentSession().createQuery("from User");
        return query.getResultList();
    }
}
