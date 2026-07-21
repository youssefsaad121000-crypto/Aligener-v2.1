package git.artdeell.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import git.artdeell.Aligner;
import it.unimi.dsi.fastutil.ints.IntList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VertexFormat.Builder.class)
public class AlignerMixin {

    @Shadow
    private int offset;

    @Inject(method = "add", at = @At("HEAD"))
    private void stashVertexMetadata(
            String string,
            VertexFormatElement vertexFormatElement,
            CallbackInfoReturnable<VertexFormat.Builder> cir,
            @Share("vertexFormatElement") LocalRef<VertexFormatElement> elementRef,
            @Share("attributeName") LocalRef<String> nameRef) {

        elementRef.set(vertexFormatElement);
        nameRef.set(string);
    }

    @WrapOperation(
            method = "add",
            at = @At(
                    value = "INVOKE",
                    target = "Lit/unimi/dsi/fastutil/ints/IntList;add(I)Z"))
    private boolean onOffsetAdded(
            IntList instance,
            int offset,
            Operation<Boolean> original,
            @Share("vertexFormatElement") LocalRef<VertexFormatElement> elementRef,
            @Share("attributeName") LocalRef<String> nameRef) {

        VertexFormatElement element = elementRef.get();

        int alignment = element.type().size();
        int misalignment = offset % alignment;

        if (misalignment != 0) {
            offset += alignment - misalignment;
            this.offset = offset;

            Aligner.LOGGER.warn(
                    "Attribute {} is not aligned (expected {}-byte alignment). New offset: {}",
                    nameRef.get(),
                    alignment,
                    offset
            );
        }

        return original.call(instance, offset);
    }
}
