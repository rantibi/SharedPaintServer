package com.sharedpaint.crud;

import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.sharedpaint.entity.BoardDrawable;
import com.sharedpaint.entity.RedoDrawable;
import com.sharedpaint.entity.RemovedDrawable;

@Stateless
public class DBQueris implements DBQuerisLocal {

	@PersistenceContext(unitName = "GlassFishEJBPaintServer")
	protected transient EntityManager em;

	@Override
	@SuppressWarnings("unchecked")
	public List<BoardDrawable> getDrawablesInBoard(long boardId, Date from,
			Date to) {

		Query q = em
				.createQuery("SELECT x FROM BoardDrawable x "
						+ "WHERE x.dateTime >= :from and x.dateTime <= :to and x.board.id = :board");
		q.setParameter("board", boardId);
		q.setParameter("from", from);
		q.setParameter("to", to);

		return (List<BoardDrawable>) q.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RemovedDrawable> getRemovedDrawableInBoard(long boardId,
			Date from, Date to) {

		Query q = em
				.createQuery("SELECT x FROM RemovedDrawable x "
						+ "WHERE x.removeDate >= :from and x.removeDate <= :to and x.board.id = :board");
		q.setParameter("board", boardId);
		q.setParameter("from", from);
		q.setParameter("to", to);

		return (List<RemovedDrawable>) q.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<String> getUsersEmailInBoard(long boardId) {
		Query q = em
				.createQuery("SELECT x.email FROM User x join x.boards board "
						+ "WHERE board.id = :board");
		q.setParameter("board", boardId);

		return (List<String>) q.getResultList();

	}

	@Override
	public BoardDrawable getLastDrawableInBoard(long boardId) {
		Query q = em.createQuery("SELECT x FROM BoardDrawable x "
				+ "WHERE x.board.id = :boardId AND "
				+ "x.dateTime = (SELECT MAX(y.dateTime) FROM BoardDrawable y "
				+ "WHERE y.board.id = :boardId)");
		q.setParameter("boardId", boardId);

		try {
			return (BoardDrawable) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public RedoDrawable getRedoDrawableInBoard(long boardId) {
		Query q = em.createQuery("SELECT x FROM RedoDrawable x "
				+ "WHERE x.board.id = :boardId AND "
				+ "x.dateTime = (SELECT MAX(y.dateTime) FROM RedoDrawable y "
				+ "WHERE y.board.id = :boardId)");
		q.setParameter("boardId", boardId);

		try {
			return (RedoDrawable) q.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public long getNextBoardSequenceId() {
		Query q = em.createNativeQuery("select nextval('drawable_seq')");
		return (long) q.getSingleResult();
	}

}
