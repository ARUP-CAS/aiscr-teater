import { FC, FocusEvent, RefObject, useRef } from 'react';
import classNames from 'classnames';

import { Button } from 'components/Button';
import useTranslation from 'hooks/useTranslation';

import { SearchInput } from './SearchInput';
import * as T from './_types';
import { Whisperer } from './Whisperer';

const formContainsChild = (
	target?: HTMLElement,
	form?: RefObject<HTMLFormElement>,
) => {
	if (!form || !form.current || !target) {
		return false;
	}

	return !!form.current.contains(target);
};

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
	const formRef = useRef<HTMLFormElement>(null);
	const t = useTranslation({
		search: { cs: 'Hledat', en: 'Search', de: 'Suche' },
		palceholder: {
			cs: 'např. Archeologické výzkumy',
			en: 'e.g. archaeological research',
			de: 'z. B. archäologische Ausgrabungen',
		},
	});

	return (
		<form
			className={classNames('search-form', {
				error,
			})}
			onSubmit={onSubmit}
			onFocus={onFocus}
			onBlur={(event: FocusEvent<HTMLFormElement>) => {
				onBlur?.(
					formContainsChild(event.relatedTarget as HTMLElement, formRef),
				);
			}}
			ref={formRef}
		>
			<div className="input-search-wrapper">
				<SearchInput
					placeholder={t.palceholder}
					loading={loading}
					error={error}
					value={value}
					{...props}
				/>
			</div>
			<Button className="button-search" title={t.search} />
			{whispererVisible ? <Whisperer data={data} hide={hideWhisperer} /> : null}
		</form>
	);
};
