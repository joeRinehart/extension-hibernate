package org.lucee.extension.orm.hibernate.event;

import lucee.runtime.Component;

import org.hibernate.event.PostUpdateEvent;
import org.hibernate.event.PostUpdateEventListener;
import org.lucee.extension.orm.hibernate.CommonUtil;

public class PostUpdateEventListenerImpl extends EventListener implements PostUpdateEventListener {

	private static final long serialVersionUID = -6636253331286381298L;

	public PostUpdateEventListenerImpl(Component component) {
	    super(component, CommonUtil.POST_UPDATE, false);
	}

	@Override
	public void onPostUpdate(PostUpdateEvent event) {
    	invoke(CommonUtil.POST_UPDATE, event.getEntity());
    }

}
