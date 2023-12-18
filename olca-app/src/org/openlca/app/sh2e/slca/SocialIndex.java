package org.openlca.app.sh2e.slca;

import org.openlca.core.model.descriptors.SocialIndicatorDescriptor;

public class SocialIndex extends DescriptorIndex<SocialIndicatorDescriptor> {

	@Override
	public SocialIndex copy() {
		var copy = new SocialIndex();
		for (var c : content) {
			copy.add(c);
		}
		return copy;
	}
}
