import { forwardRef, PropsWithChildren } from 'react';
import 'scss/_markdown.scss';

const StyledContent = forwardRef<HTMLDivElement, PropsWithChildren<unknown>>(({ children }, ref) => (
	<div className="markdown" ref={ref}>
		{children}
	</div>
));

export { StyledContent };
