package com.laioffer.onlineOrder.dao;

import com.laioffer.onlineOrder.entity.Cart;
import com.laioffer.onlineOrder.entity.OrderItem;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CartDao {

  @Autowired
  private SessionFactory sessionFactory;

  public void removeCartItem(int cartItemId) {
    Session session = null;
    try {
      session = sessionFactory.openSession();
      OrderItem cartItem = session.get(OrderItem.class, cartItemId);
      Cart cart = cartItem.getCart();
      cart.getOrderItemList().remove(cartItem);
      // remove this cartItem from cart itemlist first (remove the foreign key)

      session.beginTransaction();
      session.delete(cartItem);
      // then delete the cartItem in database (remove the primary key)
      session.getTransaction().commit();

    } catch (Exception ex) {
      ex.printStackTrace();
      if (session != null) {
        session.getTransaction().rollback();
      }
    } finally {
      if (session != null) {
        session.close();
      }
    }

  }

  public void removeAllCartItems(Cart cart) {
    for (OrderItem item : cart.getOrderItemList()) {
      removeCartItem(item.getId());
    }
  }
}

