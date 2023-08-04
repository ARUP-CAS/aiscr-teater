import { FC } from 'react';
import classNames from 'classnames';

export const Content: FC<{ className?: string; fluid?: boolean }> = ({
  children,
  fluid = false,
  className = ''
}) => (
  <div
    className={classNames(
      { content: !fluid, 'content-fluid': fluid },
      className
    )}
  >
    {children}
  </div>
);
