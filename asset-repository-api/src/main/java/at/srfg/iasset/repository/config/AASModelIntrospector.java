package at.srfg.iasset.repository.config;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.jsontype.TypeResolverBuilder;

public class AASModelIntrospector extends JacksonAnnotationIntrospector  {
    private static final long serialVersionUID = 1L;

    private static final String MODEL_TYPE_PROPERTY = "modelType";
    private static final String GETTER_PREFIX = "get";

    @Override
    public String findTypeName(AnnotatedClass ac) {
        String customType = AASModelHelper.getTypeProperty(ac.getRawType());
        return customType != null
                ? customType
                : super.findTypeName(ac);
    }

    @Override
    public TypeResolverBuilder<?> findTypeResolver(MapperConfig<?> config, AnnotatedClass ac, JavaType baseType) {
        String modelType = AASModelHelper.getTypePropertyName(ac.getRawType());
        if (modelType != null) {
            TypeResolverBuilder<?> result = _constructStdTypeResolverBuilder();
            result = result.init(JsonTypeInfo.Id.NAME, null);
            result.inclusion(JsonTypeInfo.As.PROPERTY);
            result.typeProperty(modelType);
            result.typeIdVisibility(false);
            return result;
        }
        return super.findTypeResolver(config, ac, baseType);
    }

    @Override
    public List<NamedType> findSubtypes(Annotated a) {
        if (AASModelHelper.SUBTYPES.containsKey(a.getRawType()) && !AASModelHelper.SUBTYPES.get(a.getRawType()).isEmpty()) {
            return AASModelHelper.SUBTYPES.get(a.getRawType()).stream()
                    .map(x -> new NamedType(x, x.getSimpleName()))
                    .collect(Collectors.toList());
        }
        return super.findSubtypes(a);
    }

    @Override
    public JsonInclude.Value findPropertyInclusion(Annotated a) {
        JsonInclude.Value result = super.findPropertyInclusion(a);
        if (result != JsonInclude.Value.empty()) {
            return result;
        }
        if (AnnotatedMethod.class.isAssignableFrom(a.getClass())) {
            AnnotatedMethod method = (AnnotatedMethod) a;
            if (method.getParameterCount() == 0
                    && method.getName().startsWith(GETTER_PREFIX)
                    && Collection.class.isAssignableFrom(method.getRawReturnType())
                    && AASModelHelper.isModelInterfaceOrDefaultImplementation(method.getDeclaringClass())) {
                return result.withValueInclusion(JsonInclude.Include.NON_EMPTY);
            }
        }
        return result;
    }

}
