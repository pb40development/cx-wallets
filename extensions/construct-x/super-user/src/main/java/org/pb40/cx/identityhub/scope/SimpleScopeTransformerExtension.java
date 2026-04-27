package org.pb40.cx.identityhub.scope;

import org.eclipse.edc.identityhub.spi.transformation.ScopeToCriterionTransformer;
import org.eclipse.edc.runtime.metamodel.annotation.Extension;
import org.eclipse.edc.runtime.metamodel.annotation.Provider;
import org.eclipse.edc.spi.system.ServiceExtension;

@Extension("Simple Dev Scope Transformer")
public class SimpleScopeTransformerExtension implements ServiceExtension {

    @Provider
    public ScopeToCriterionTransformer createScopeTransformer() {
        return new SimpleScopeTransformer();
    }
}
