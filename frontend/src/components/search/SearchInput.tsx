import React, { FC, InputHTMLAttributes } from 'react'
import classNames from 'classnames'

export const SearchInput: FC<
  InputHTMLAttributes<HTMLInputElement> & {
    loading?: boolean
    error?: boolean
  }
> = ({ loading = false, error = false, ...props }): JSX.Element => (
  <input
    className={classNames('input-search', { loading, error })}
    autoComplete="off"
    name="search"
    {...props}
  />
)
