package com.sgu.tetris3d;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	private TetrisGLSurface gl_surface_view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		gl_surface_view = (TetrisGLSurface) findViewById(R.id.frag_glsurfaceview);
		
		/* if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.frag_glsurfaceview, new GLSurfaceViewFragment()).commit();
		} */
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		gl_surface_view.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		gl_surface_view.onResume();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/* public static class GLSurfaceViewFragment extends Fragment {

		public GLSurfaceViewFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View rootView = new TetrisGLSurface(getActivity());
			return rootView;
		}
	} */
}
