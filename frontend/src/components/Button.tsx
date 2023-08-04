import { FC } from 'react';

export const Button: FC<{
  className?: string
  type?: 'submit' | 'reset' | 'button'
  title: string
}> = ({ className = '', type = 'submit', title }) => (
  <button type={type} className={className}>
    {title}
  </button>
);
