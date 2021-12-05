package shadows.hostilenetworks.client;

import com.mojang.blaze3d.vertex.IVertexBuilder;

public class GhostVertexBuilder implements IVertexBuilder {
	private final IVertexBuilder wrapped;
	private final int alpha;

	public GhostVertexBuilder(IVertexBuilder wrapped, int alpha) {
		this.wrapped = wrapped;
		this.alpha = alpha;
	}

	@Override
	public IVertexBuilder vertex(double x, double y, double z) {
		return this.wrapped.vertex(x, y, z);
	}

	@Override
	public IVertexBuilder color(int red, int green, int blue, int alpha) {
		return this.wrapped.color(red, green, blue, alpha * this.alpha / 0xFF);
	}

	@Override
	public IVertexBuilder uv(float u, float v) {
		return this.wrapped.uv(u, v);
	}

	@Override
	public IVertexBuilder overlayCoords(int u, int v) {
		return this.wrapped.overlayCoords(u, v);
	}

	@Override
	public IVertexBuilder uv2(int u, int v) {
		return this.wrapped.uv2(u, v);
	}

	@Override
	public IVertexBuilder normal(float x, float y, float z) {
		return this.wrapped.normal(x, y, z);
	}

	@Override
	public void endVertex() {
		this.wrapped.endVertex();
	}

}