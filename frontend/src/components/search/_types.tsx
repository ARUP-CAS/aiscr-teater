import { FocusEventHandler, FormEventHandler, InputHTMLAttributes } from 'react';

// eslint-disable-next-line @typescript-eslint/camelcase
import { GetSearchResults_search } from 'generated/GetSearchResults';

// eslint-disable-next-line @typescript-eslint/camelcase
export type FormResult = GetSearchResults_search

export type SearchFormsAttributes = {
  data?: FormResult[]
  loading?: boolean
  error?: boolean
  value?: string
  whispererVisible?: boolean
  onSubmit?: FormEventHandler<HTMLFormElement>
  onFocus?: FocusEventHandler<HTMLFormElement>
  onBlur?: (relatedTargetIsChildren: boolean) => void
  hideWhisperer?: () => void
}

export type SearchFormsProps = SearchFormsAttributes &
  Omit<InputHTMLAttributes<HTMLInputElement>, 'onSubmit' | 'onFocus' | 'onBlur'>
