package com.paulormg.recommendex.server.services.stats;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.paulormg.recommendex.client.services.stats.StatisticsService;
import com.paulormg.recommendex.server.dao.stats.StatisticsDAO;
import com.paulormg.recommendex.shared.transfer.User;

@SuppressWarnings("serial")
public class StatisticsServiceImpl extends RemoteServiceServlet implements StatisticsService {

	static Logger logger = Logger.getLogger(StatisticsServiceImpl.class);
	private StatisticsDAO statsDao = StatisticsDAO.getInstance();

	@Override
	public void viewedItem(int itemId) {
		// no authentication (best effort)

		HttpSession session = getThreadLocalRequest().getSession(false);
		if (session == null)
			return;

		Object obj = session.getAttribute("user");
		if (obj == null)
			return;

		User user = (User)obj;

		logger.info(String.format("User %d in session %s has viewed item %d.", 
				user.getId(), session.getId(), itemId));

		statsDao.viewedItem(user.getId(), session.getId(), itemId);
	}

	@Override
	public void addedItemToCart(int itemId) {
		HttpSession session = getThreadLocalRequest().getSession(false);
		if (session == null)
			return;

		Object obj = session.getAttribute("user");
		if (obj == null)
			return;

		User user = (User)obj;

		logger.info(String.format("User %d in session %s has added item %d to cart.", 
				user.getId(), session.getId(), itemId));

		statsDao.addedItemToCart(user.getId(), session.getId(), itemId);
	}

	public static AsyncCallback<Void> getStatisticsCallback() {
		return new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				// best-effort stats logging: do nothing
			}

			@Override
			public void onSuccess(Void result) {
				// best-effort stats logging: do nothing
			}
		};
	}
}
