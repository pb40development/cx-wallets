package org.pb40.cx.identityhub.scope;

import org.eclipse.edc.identityhub.spi.transformation.ScopeToCriterionTransformer;
import org.eclipse.edc.spi.query.Criterion;
import org.eclipse.edc.spi.result.Result;

import java.util.List;

import static org.eclipse.edc.spi.result.Result.failure;
import static org.eclipse.edc.spi.result.Result.success;

/**
 * A ScopeToCriterionTransformer that accepts simple "<namespace>:<credentialType>" scopes.
 */
public class SimpleScopeTransformer implements ScopeToCriterionTransformer {

    public static final String TYPE_OPERAND = "verifiableCredential.credential.type";
    public static final String CONTAINS_OPERATOR = "contains";
    private static final String SCOPE_SEPARATOR = ":";

    @Override
    public Result<Criterion> transform(String scope) {
        var tokens = tokenize(scope);
        if (tokens.failed()) {
            return failure("Scope string cannot be converted: %s".formatted(tokens.getFailureDetail()));
        }
        var credentialType = tokens.getContent()[1];
        return success(new Criterion(TYPE_OPERAND, CONTAINS_OPERATOR, credentialType));
    }

    private Result<String[]> tokenize(String scope) {
        if (scope == null) return failure("Scope was null");
        var tokens = scope.split(SCOPE_SEPARATOR);
        return success(tokens);
    }
}
