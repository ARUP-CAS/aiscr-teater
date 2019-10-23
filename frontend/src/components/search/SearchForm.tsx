import React, { FC, FocusEvent, RefObject, useRef } from 'react'
import classNames from 'classnames'
import { SearchInput } from './SearchInput'
import { Button } from 'components/Button'
import * as T from './_types'
import { Whisperer } from './Whisperer'

function formContainsChild(
  target?: HTMLElement,
  form?: RefObject<HTMLFormElement>
): boolean {
  if (!form || !form.current || !target) {
    return false
  }

  return !!form.current.contains(target)
}

export const SearchForm: FC<T.SearchFormsProps> = ({
  data = [],
  loading,
  value = '',
  whispererVisible = true,
  error = false,
  onSubmit,
  onFocus,
  onBlur,
  hideWhisperer,
  ...props
}): JSX.Element => {
  const formRef = useRef<HTMLFormElement>(null)

  return (
    <form
      className={classNames('search-form', {
        error
      })}
      onSubmit={onSubmit}
      onFocus={onFocus}
      onBlur={(event: FocusEvent<HTMLFormElement>) => {
        onBlur &&
          onBlur(formContainsChild(event.relatedTarget as HTMLElement, formRef))
      }}
      ref={formRef}
    >
      <div className="input-search-wrapper">
        <SearchInput
          placeholder="např. Archeologické výzkumy"
          loading={loading}
          error={error}
          value={value}
          {...props}
        />
      </div>
      <Button className="button-search" title="Hledat" />
      {whispererVisible ? <Whisperer data={data} hide={hideWhisperer} /> : null}
    </form>
  )
}
