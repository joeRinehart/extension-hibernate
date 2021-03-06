package org.lucee.extension.orm.hibernate.tuplizer.accessors;

import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;

import lucee.loader.engine.CFMLEngineFactory;
import lucee.runtime.Component;
import lucee.runtime.PageContext;
import lucee.runtime.exp.PageException;
import lucee.runtime.orm.ORMSession;
import lucee.runtime.type.Collection;
import lucee.runtime.type.Collection.Key;

import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.property.Getter;
import org.hibernate.type.Type;
import org.lucee.extension.orm.hibernate.CommonUtil;
import org.lucee.extension.orm.hibernate.HibernateCaster;
import org.lucee.extension.orm.hibernate.HibernateORMEngine;
import org.lucee.extension.orm.hibernate.HibernatePageException;
import org.lucee.extension.orm.hibernate.HibernateUtil;

public class CFCGetter implements Getter {

	private Key key;

	/**
	 * Constructor of the class
	 * @param key
	 */
	public CFCGetter(String key){
		this(CommonUtil.createKey(key));
	}
	
	/**
	 * Constructor of the class
	 * @param engine 
	 * @param key
	 */
	public CFCGetter( Collection.Key key){
		this.key=key;
	}
	
	@Override
	public Object get(Object trg) throws HibernateException {
		try {
			// MUST cache this, perhaps when building xml
			PageContext pc = CommonUtil.pc(); // pc can be null
			ORMSession session = pc.getORMSession(true);
			Component cfc = CommonUtil.toComponent(trg);
			String dsn = CFMLEngineFactory.getInstance().getORMUtil().getDataSourceName(pc, cfc);
			String name = HibernateCaster.getEntityName(cfc);
			SessionFactory sf=(SessionFactory) session.getRawSessionFactory(dsn);
			ClassMetadata metaData = sf.getClassMetadata(name);
			Type type = HibernateUtil.getPropertyType(metaData, key.getString());

			Object rtn = cfc.getComponentScope().get(key,null);
			return HibernateCaster.toSQL(type, rtn,null);
		} 
		catch (PageException pe) {
			throw new HibernatePageException(pe);
		}
		catch (Exception e) {
			throw new HibernatePageException(CFMLEngineFactory.getInstance().getCastUtil().toPageException(e));
		}
	}
	

	public HibernateORMEngine getHibernateORMEngine(){
		try {
			// TODO better impl
			return HibernateUtil.getORMEngine(CommonUtil.pc());
		} 
		catch (PageException e) {}
			
		return null;
	}
	

	@Override
	public Object getForInsert(Object trg, Map arg1, SessionImplementor arg2)throws HibernateException {
		return get(trg);// MUST better solution? this is from MapGetter
	}

	@Override
	public Member getMember() {
		return null;
	}

	@Override
	public Method getMethod() {
		return null;
	}

	@Override
	public String getMethodName() {
		return null;// MUST macht es sinn den namen zurueck zu geben?
	}

	@Override
	public Class getReturnType() {
		return Object.class;// MUST more concrete?
	}

}
